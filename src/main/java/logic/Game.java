package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.ActionCards;
import model.Card;
import model.DrawPileAndDiscardPile;
import model.MoneyCards;
import model.PropertiesCards;
import model.Player;
import model.PropertyColor;

/**
 * Central state machine for a Monopoly Deal game.
 *
 * <p>Rules implemented (per the simplified spec):
 * <ul>
 *   <li>Three card categories: money (1/4/10M), property (single-colored) and rent.</li>
 *   <li>Each player is dealt 5 cards at the start of the game.</li>
 *   <li>On every turn the current player draws 2 cards (or 5 if hand is empty)
 *       then may play at most one card of each category.</li>
 *   <li>Money goes to the player's bank; properties go to the property area;
 *       rent demands a payment from a chosen opponent in the rent card's color.
 *       Rent is only legal if both the attacker and target own properties of
 *       that color.</li>
 *   <li>Rent is paid from the payer's bank using the minimum sufficient subset
 *       (no change given; if bank total is insufficient, everything in the
 *       bank is transferred).</li>
 *   <li>At end of turn any hand cards above seven are returned to the bottom
 *       of the draw pile.</li>
 *   <li>First player to complete three full property sets wins.</li>
 * </ul>
 */
public class Game {

    // --- UI sizing constants kept for backwards compatibility ---------------
    public static final double SCREEN_WIDTH = 1100;
    public static final double SCREEN_HEIGHT = 720;

    // --- rule constants -----------------------------------------------------
    public static final int INITIAL_HAND = 5;
    public static final int DRAW_PER_TURN = 2;
    public static final int DRAW_WHEN_EMPTY = 5;
    public static final int HAND_LIMIT = 7;
    public static final int MAX_MONEY_PER_TURN = 1;
    public static final int MAX_PROPERTY_PER_TURN = 1;
    public static final int MAX_RENT_PER_TURN = 1;
    public static final int SETS_TO_WIN = 3;

    private final List<Player> players;
    private final DrawPileAndDiscardPile deck;
    private final List<String> log = new ArrayList<>();

    private int currentIndex;
    private GamePhase phase = GamePhase.DRAW;
    private int moneyPlays;
    private int propertyPlays;
    private int rentPlays;
    private RentRequest pendingRent;
    private Player winner;

    // --- construction -------------------------------------------------------

    public Game() {
        this(Arrays.asList("Player 1", "Player 2"));
    }

    public Game(List<String> playerNames) {
        this(playerNames, new DrawPileAndDiscardPile());
    }

    public Game(List<String> playerNames, DrawPileAndDiscardPile deck) {
        if (playerNames == null || playerNames.size() < 2) {
            throw new IllegalArgumentException("at least two players required");
        }
        this.deck = deck;
        this.players = new ArrayList<>();
        for (String n : playerNames) players.add(new Player(n));
        for (Player p : players) p.drawFrom(deck, INITIAL_HAND);
    }

    // --- turn lifecycle -----------------------------------------------------

    /** Begins the first turn. Draws for the first player. */
    public void startGame() {
        currentIndex = 0;
        startTurn();
    }

    private void startTurn() {
        Player p = currentPlayer();
        int n = p.getHand().isEmpty() ? DRAW_WHEN_EMPTY : DRAW_PER_TURN;
        p.drawFrom(deck, n);
        phase = GamePhase.PLAY;
        moneyPlays = propertyPlays = rentPlays = 0;
        log(p.getName() + " drew " + n + " card(s)");
    }

    /** Request end of current turn. Transitions to DISCARD if hand > 7. */
    public void endTurn() {
        if (phase == GamePhase.AWAITING_PAYMENT) {
            throw new IllegalStateException("resolve pending rent first");
        }
        if (phase == GamePhase.GAME_OVER) return;
        Player p = currentPlayer();
        if (p.getHand().size() > HAND_LIMIT) {
            phase = GamePhase.DISCARD;
            log(p.getName() + " must discard down to " + HAND_LIMIT + " cards");
            return;
        }
        advanceToNextPlayer();
    }

    private void advanceToNextPlayer() {
        currentIndex = (currentIndex + 1) % players.size();
        startTurn();
    }

    // --- play actions -------------------------------------------------------

    /** Whether the card may currently be played by the current player. */
    public boolean canPlay(Card card) {
        if (phase != GamePhase.PLAY) return false;
        if (!currentPlayer().getHand().contains(card)) return false;
        if (card instanceof MoneyCards) {
            return moneyPlays < MAX_MONEY_PER_TURN;
        }
        if (card instanceof PropertiesCards) {
            return propertyPlays < MAX_PROPERTY_PER_TURN;
        }
        if (card instanceof ActionCards rent) {
            return rentPlays < MAX_RENT_PER_TURN
                    && currentPlayer().propertyCount(rent.getColor()) > 0
                    && !validRentTargets(rent.getColor()).isEmpty();
        }
        return false;
    }

    public void playMoney(MoneyCards card) {
        requirePhase(GamePhase.PLAY);
        if (moneyPlays >= MAX_MONEY_PER_TURN) {
            throw new IllegalStateException("already played a money card this turn");
        }
        currentPlayer().playMoney(card);
        moneyPlays++;
        log(currentPlayer().getName() + " banked " + card.getDisplay());
        checkWin();
    }

    public void playProperty(PropertiesCards card) {
        requirePhase(GamePhase.PLAY);
        if (propertyPlays >= MAX_PROPERTY_PER_TURN) {
            throw new IllegalStateException("already played a property card this turn");
        }
        currentPlayer().playProperty(card);
        propertyPlays++;
        log(currentPlayer().getName() + " placed " + card.getDisplay() + " property");
        checkWin();
    }

    /**
     * Plays a rent card against {@code target}. Moves the rent card to the
     * discard pile and transitions to AWAITING_PAYMENT.
     */
    public void playRent(ActionCards card, Player target) {
        requirePhase(GamePhase.PLAY);
        if (rentPlays >= MAX_RENT_PER_TURN) {
            throw new IllegalStateException("already played a rent card this turn");
        }
        if (target == null || target == currentPlayer() || !players.contains(target)) {
            throw new IllegalArgumentException("invalid rent target");
        }
        Player attacker = currentPlayer();
        if (attacker.propertyCount(card.getColor()) == 0) {
            throw new IllegalStateException("attacker does not own properties of "
                    + card.getColor());
        }
        if (target.propertyCount(card.getColor()) == 0) {
            throw new IllegalStateException("target does not own properties of "
                    + card.getColor());
        }
        attacker.discardHandCard(card, deck);
        int amount = attacker.propertyCount(card.getColor()) * ActionCards.RENT_PER_PROPERTY;
        pendingRent = new RentRequest(attacker, target, card.getColor(), amount);
        phase = GamePhase.AWAITING_PAYMENT;
        rentPlays++;
        log(attacker.getName() + " demands $" + amount + "M rent on "
                + card.getColor().getDisplayName() + " from " + target.getName());
    }

    /**
     * Settles the pending rent using the payer's minimum sufficient payment
     * (see {@link Player#computeMinSufficientPayment(int)}). Idempotent once
     * the phase has moved back to PLAY.
     */
    public void resolveRent() {
        if (phase != GamePhase.AWAITING_PAYMENT || pendingRent == null) return;
        List<MoneyCards> payment =
                pendingRent.getPayer().computeMinSufficientPayment(pendingRent.getAmount());
        int paid = payment.stream().mapToInt(MoneyCards::getValue).sum();
        pendingRent.getPayer().pay(payment, pendingRent.getCollector());
        log(pendingRent.getPayer().getName() + " paid $" + paid + "M (demanded $"
                + pendingRent.getAmount() + "M)");
        pendingRent = null;
        phase = GamePhase.PLAY;
        checkWin();
    }

    // --- discard phase ------------------------------------------------------

    public void discardExcess(Card card) {
        requirePhase(GamePhase.DISCARD);
        Player p = currentPlayer();
        p.returnHandCardToDeck(card, deck);
        log(p.getName() + " returned a card to the deck");
        if (p.getHand().size() <= HAND_LIMIT) {
            advanceToNextPlayer();
        }
    }

    // --- introspection used by the GUI and tests ----------------------------

    public Player currentPlayer() { return players.get(currentIndex); }
    public int getCurrentPlayerIndex() { return currentIndex; }
    public GamePhase getPhase() { return phase; }
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public DrawPileAndDiscardPile getDeck() { return deck; }
    public RentRequest getPendingRent() { return pendingRent; }
    public Player getWinner() { return winner; }
    public boolean isWin() { return winner != null; }
    public List<String> getLog() { return Collections.unmodifiableList(log); }

    public int remainingMoneyPlays()    { return MAX_MONEY_PER_TURN - moneyPlays; }
    public int remainingPropertyPlays() { return MAX_PROPERTY_PER_TURN - propertyPlays; }
    public int remainingRentPlays()     { return MAX_RENT_PER_TURN - rentPlays; }

    /** Opponents of the current player, in seat order. */
    public List<Player> opponents() {
        List<Player> list = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i != currentIndex) list.add(players.get(i));
        }
        return list;
    }

    /** Opponents of the current player that own at least one property of {@code color}. */
    public List<Player> validRentTargets(PropertyColor color) {
        List<Player> list = new ArrayList<>();
        for (Player p : opponents()) {
            if (p.propertyCount(color) > 0) list.add(p);
        }
        return list;
    }

    // --- helpers ------------------------------------------------------------

    private void checkWin() {
        for (Player p : players) {
            if (p.completeSetCount() >= SETS_TO_WIN) {
                winner = p;
                phase = GamePhase.GAME_OVER;
                log(p.getName() + " WINS with " + p.completeSetCount() + " complete sets!");
                return;
            }
        }
    }

    private void requirePhase(GamePhase expected) {
        if (phase != expected) {
            throw new IllegalStateException("expected phase " + expected + " but was " + phase);
        }
    }

    private void log(String msg) {
        log.add(msg);
    }
}
