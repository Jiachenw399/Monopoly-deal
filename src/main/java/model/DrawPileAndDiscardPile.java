package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Draw pile and discard pile for the deck. Manages deck composition,
 * shuffling, drawing, discarding and returning cards to the bottom of the
 * draw pile (used when a player has to get rid of excess hand cards).
 */
public class DrawPileAndDiscardPile {

    /** Standard deck composition, exposed for tests / UI summaries. */
    public static final int MONEY_1_COUNT = 6;
    public static final int MONEY_4_COUNT = 5;
    public static final int MONEY_10_COUNT = 2;
    public static final int EXTRA_PROPERTIES_PER_COLOR = 2;
    public static final int RENT_PER_COLOR = 2;

    private final List<Card> drawPile = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final Random random;

    public DrawPileAndDiscardPile() {
        this(new Random());
    }

    public DrawPileAndDiscardPile(Random random) {
        this.random = random;
        buildDeck();
        shuffleDrawPile();
    }

    private void buildDeck() {
        for (int i = 0; i < MONEY_1_COUNT; i++)  drawPile.add(new MoneyCards(1));
        for (int i = 0; i < MONEY_4_COUNT; i++)  drawPile.add(new MoneyCards(4));
        for (int i = 0; i < MONEY_10_COUNT; i++) drawPile.add(new MoneyCards(10));

        for (PropertyColor c : PropertyColor.values()) {
            int count = c.getSetSize() + EXTRA_PROPERTIES_PER_COLOR;
            for (int i = 0; i < count; i++) {
                drawPile.add(new PropertiesCards(c));
            }
            for (int i = 0; i < RENT_PER_COLOR; i++) {
                drawPile.add(new ActionCards(c));
            }
        }
    }

    public final void shuffleDrawPile() {
        Collections.shuffle(drawPile, random);
    }

    /**
     * Draws the top card. Automatically reshuffles the discard pile into the
     * draw pile if the draw pile is empty. Returns {@code null} only if both
     * piles are empty.
     */
    public Card draw() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }
        if (drawPile.isEmpty()) {
            return null;
        }
        return drawPile.remove(drawPile.size() - 1);
    }

    private void reshuffleDiscardIntoDraw() {
        drawPile.addAll(discardPile);
        discardPile.clear();
        shuffleDrawPile();
    }

    /** Puts a card on top of the discard pile. */
    public void discard(Card card) {
        discardPile.add(card);
    }

    /** Returns a card to the bottom of the draw pile (used for hand-limit discards). */
    public void returnToBottom(Card card) {
        drawPile.add(0, card);
    }

    public int drawPileSize() {
        return drawPile.size();
    }

    public int discardPileSize() {
        return discardPile.size();
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }
}
