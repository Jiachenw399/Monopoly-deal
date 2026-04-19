package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.ActionCards;
import model.MoneyCards;
import model.PropertiesCards;
import model.Player;
import model.PropertyColor;

class GameTest {

    private Game newTwoPlayerGame() {
        return new Game(List.of("Alice", "Bob"));
    }

    @Test
    void initialDealGivesFiveCardsEach() {
        Game g = newTwoPlayerGame();
        for (Player p : g.getPlayers()) {
            assertEquals(Game.INITIAL_HAND, p.getHand().size());
        }
    }

    @Test
    void startGameDrawsForFirstPlayerAndEntersPlayPhase() {
        Game g = newTwoPlayerGame();
        int before = g.getPlayers().get(0).getHand().size();
        g.startGame();
        assertEquals(GamePhase.PLAY, g.getPhase());
        assertEquals(before + Game.DRAW_PER_TURN, g.currentPlayer().getHand().size());
    }

    @Test
    void startTurnDrawsFiveIfHandEmpty() {
        Game g = newTwoPlayerGame();
        g.currentPlayer().getHand().clear();
        g.startGame();
        assertEquals(Game.DRAW_WHEN_EMPTY, g.currentPlayer().getHand().size());
    }

    @Test
    void playerMayPlayOnlyOneMoneyPerTurn() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        MoneyCards m1 = new MoneyCards(1);
        MoneyCards m2 = new MoneyCards(4);
        me.getHand().add(m1);
        me.getHand().add(m2);
        g.playMoney(m1);
        assertFalse(g.canPlay(m2));
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> g.playMoney(m2));
        assertTrue(ex.getMessage() != null);
    }

    @Test
    void playerMayPlayOnlyOnePropertyPerTurn() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        PropertiesCards p1 = new PropertiesCards(PropertyColor.RED);
        PropertiesCards p2 = new PropertiesCards(PropertyColor.BLACK);
        me.getHand().add(p1);
        me.getHand().add(p2);
        g.playProperty(p1);
        assertFalse(g.canPlay(p2));
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> g.playProperty(p2));
        assertTrue(ex.getMessage() != null);
    }

    @Test
    void rentRequiresAttackerToOwnColor() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        ActionCards rent = new ActionCards(PropertyColor.RED);
        me.getHand().add(rent);
        assertFalse(g.canPlay(rent));
    }

    @Test
    void rentFlowTransfersMinimumSufficientPayment() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        Player opp = g.opponents().get(0);

        // Attacker owns one RED property.
        me.getProperties().get(PropertyColor.RED)
                .add(new PropertiesCards(PropertyColor.RED));
        // Target owns one RED property (required for rent to be legal).
        opp.getProperties().get(PropertyColor.RED)
                .add(new PropertiesCards(PropertyColor.RED));
        // Target bank = [1,1,1,10]; demanded = 1 * 2 = 2 -> best = 1+1 = 2.
        opp.getBank().clear();
        opp.getBank().add(new MoneyCards(1));
        opp.getBank().add(new MoneyCards(1));
        opp.getBank().add(new MoneyCards(1));
        opp.getBank().add(new MoneyCards(10));

        ActionCards rent = new ActionCards(PropertyColor.RED);
        me.getHand().add(rent);

        assertTrue(g.canPlay(rent));
        g.playRent(rent, opp);
        assertEquals(GamePhase.AWAITING_PAYMENT, g.getPhase());
        assertEquals(2, g.getPendingRent().getAmount());

        int meBankBefore = me.bankTotal();
        g.resolveRent();
        assertEquals(GamePhase.PLAY, g.getPhase());
        assertEquals(meBankBefore + 2, me.bankTotal()); // received exactly 2M
        assertEquals(2, opp.getBank().size());          // still has [1, 10]
    }

    @Test
    void rentCannotBePlayedIfTargetHasNoSuchColor() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        me.getProperties().get(PropertyColor.RED)
                .add(new PropertiesCards(PropertyColor.RED));
        ActionCards rent = new ActionCards(PropertyColor.RED);
        me.getHand().add(rent);
        // no opponent owns RED
        assertFalse(g.canPlay(rent));
    }

    @Test
    void endTurnEntersDiscardPhaseWhenHandTooLarge() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        while (me.getHand().size() <= Game.HAND_LIMIT) {
            me.getHand().add(new MoneyCards(1));
        }
        g.endTurn();
        assertEquals(GamePhase.DISCARD, g.getPhase());
        assertEquals(me, g.currentPlayer()); // still the same player
    }

    @Test
    void discardExcessAdvancesTurnOnceBelowLimit() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        while (me.getHand().size() <= Game.HAND_LIMIT) {
            me.getHand().add(new MoneyCards(1));
        }
        g.endTurn();
        // discard one card -> still over limit? fill to exactly limit+1 to be
        // deterministic.
        while (me.getHand().size() > Game.HAND_LIMIT + 1) {
            g.discardExcess(me.getHand().get(me.getHand().size() - 1));
        }
        assertEquals(GamePhase.DISCARD, g.getPhase());
        g.discardExcess(me.getHand().get(me.getHand().size() - 1));
        assertEquals(GamePhase.PLAY, g.getPhase());
        assertEquals("Bob", g.currentPlayer().getName());
    }

    @Test
    void gameEndsWhenPlayerCompletesThreeSets() {
        Game g = newTwoPlayerGame();
        g.startGame();
        Player me = g.currentPlayer();
        // Pre-stage two full sets.
        me.getProperties().get(PropertyColor.BROWN)
                .add(new PropertiesCards(PropertyColor.BROWN));
        me.getProperties().get(PropertyColor.BROWN)
                .add(new PropertiesCards(PropertyColor.BROWN));
        me.getProperties().get(PropertyColor.DARK_BLUE)
                .add(new PropertiesCards(PropertyColor.DARK_BLUE));
        me.getProperties().get(PropertyColor.DARK_BLUE)
                .add(new PropertiesCards(PropertyColor.DARK_BLUE));
        // Third set still needs one more LIGHT_GREEN.
        me.getProperties().get(PropertyColor.LIGHT_GREEN)
                .add(new PropertiesCards(PropertyColor.LIGHT_GREEN));
        PropertiesCards winning = new PropertiesCards(PropertyColor.LIGHT_GREEN);
        me.getHand().add(winning);

        g.playProperty(winning);
        assertEquals(GamePhase.GAME_OVER, g.getPhase());
        assertEquals(me, g.getWinner());
    }
}
