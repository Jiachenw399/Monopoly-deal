package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A Monopoly Deal player. Each player owns three private card areas:
 *
 *  - hand: cards not yet played
 *  - bank: money cards played face up; the ONLY source used to pay rent
 *  - properties: property cards grouped by color
 *
 * Player is a pure data / rules object; it knows how to draw, play and pay
 * but does not drive turn flow. That is the job of {@code logic.Game}.
 */
public class Player {

    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private final List<MoneyCards> bank = new ArrayList<>();
    private final Map<PropertyColor, List<PropertiesCards>> properties =
            new EnumMap<>(PropertyColor.class);

    public Player(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("player name required");
        }
        this.name = name;
        for (PropertyColor c : PropertyColor.values()) {
            properties.put(c, new ArrayList<>());
        }
    }

    // --------- accessors ----------------------------------------------------

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<MoneyCards> getBank() {
        return bank;
    }

    public Map<PropertyColor, List<PropertiesCards>> getProperties() {
        return properties;
    }

    public int bankTotal() {
        int sum = 0;
        for (MoneyCards c : bank) sum += c.getValue();
        return sum;
    }

    public int propertyCount(PropertyColor color) {
        return properties.get(color).size();
    }

    public int completeSetCount() {
        int n = 0;
        for (PropertyColor c : PropertyColor.values()) {
            if (properties.get(c).size() >= c.getSetSize()) n++;
        }
        return n;
    }

    /** A player wins when they own three complete property sets. */
    public boolean hasWon() {
        return completeSetCount() >= 3;
    }

    // --------- hand actions -------------------------------------------------

    /** Draws up to {@code count} cards from the deck into the hand. */
    public void drawFrom(DrawPileAndDiscardPile deck, int count) {
        for (int i = 0; i < count; i++) {
            Card c = deck.draw();
            if (c == null) break;
            hand.add(c);
        }
    }

    public void playMoney(MoneyCards card) {
        removeFromHand(card);
        bank.add(card);
    }

    public void playProperty(PropertiesCards card) {
        removeFromHand(card);
        properties.get(card.getColor()).add(card);
    }

    /** Moves a card out of the hand and into the deck's discard pile. */
    public void discardHandCard(Card card, DrawPileAndDiscardPile deck) {
        removeFromHand(card);
        deck.discard(card);
    }

    /** Returns a hand card to the bottom of the draw pile (excess discard at turn end). */
    public void returnHandCardToDeck(Card card, DrawPileAndDiscardPile deck) {
        removeFromHand(card);
        deck.returnToBottom(card);
    }

    private void removeFromHand(Card card) {
        if (!hand.remove(card)) {
            throw new IllegalStateException("card " + card + " is not in hand of " + name);
        }
    }

    // --------- rent payment -------------------------------------------------

    /**
     * Computes the minimum sufficient payment from the bank for the given amount.
     *
     * Rule: pick the subset of bank cards whose total is smallest while still
     *       being {@code >= amount}. Ties are broken by fewest cards.
     *       If the bank total is itself less than {@code amount} the payer
     *       hands over their whole bank (short pay, no change).
     *
     * The bank rarely exceeds ~20 cards, so an O(2^n) subset search is safe.
     */
    public List<MoneyCards> computeMinSufficientPayment(int amount) {
        if (amount <= 0) return Collections.emptyList();
        if (bankTotal() <= amount) return new ArrayList<>(bank);

        int n = bank.size();
        int bestMask = 0;
        int bestSum = Integer.MAX_VALUE;
        int bestCount = Integer.MAX_VALUE;

        for (int mask = 1; mask < (1 << n); mask++) {
            int sum = 0;
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sum += bank.get(i).getValue();
                    cnt++;
                }
            }
            if (sum >= amount
                    && (sum < bestSum || (sum == bestSum && cnt < bestCount))) {
                bestSum = sum;
                bestCount = cnt;
                bestMask = mask;
            }
        }

        List<MoneyCards> chosen = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) chosen.add(bank.get(i));
        }
        return chosen;
    }

    /** Transfers the given bank cards to {@code receiver}. Validates ownership. */
    public void pay(List<MoneyCards> cards, Player receiver) {
        // validate first so the transfer is atomic
        for (MoneyCards c : cards) {
            if (!bank.contains(c)) {
                throw new IllegalStateException("card " + c + " is not in bank of " + name);
            }
        }
        for (MoneyCards c : cards) {
            bank.remove(c);
            receiver.bank.add(c);
        }
    }
}

