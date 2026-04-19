package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class DrawPileAndDiscardPileTest {

    @Test
    void deckBuildsExpectedTotalCardCount() {
        DrawPileAndDiscardPile deck = new DrawPileAndDiscardPile(new Random(0));
        int expectedMoney = DrawPileAndDiscardPile.MONEY_1_COUNT
                + DrawPileAndDiscardPile.MONEY_4_COUNT
                + DrawPileAndDiscardPile.MONEY_10_COUNT;
        int expectedProperties = 0;
        for (PropertyColor c : PropertyColor.values()) {
            expectedProperties += c.getSetSize()
                    + DrawPileAndDiscardPile.EXTRA_PROPERTIES_PER_COLOR;
        }
        int expectedRent = PropertyColor.values().length
                * DrawPileAndDiscardPile.RENT_PER_COLOR;
        assertEquals(expectedMoney + expectedProperties + expectedRent,
                deck.drawPileSize());
    }

    @Test
    void drawReducesPileAndReturnsCard() {
        DrawPileAndDiscardPile deck = new DrawPileAndDiscardPile(new Random(1));
        int before = deck.drawPileSize();
        Card c = deck.draw();
        assertNotNull(c);
        assertEquals(before - 1, deck.drawPileSize());
    }

    @Test
    void drawReshufflesDiscardWhenDrawPileEmpty() {
        DrawPileAndDiscardPile deck = new DrawPileAndDiscardPile(new Random(2));
        // drain the pile
        while (deck.draw() != null) { /* drain */ }
        // put one card into the discard pile
        deck.discard(new MoneyCards(1));
        assertEquals(0, deck.drawPileSize());
        Card c = deck.draw();
        assertNotNull(c);
    }

    @Test
    void drawReturnsNullWhenBothPilesEmpty() {
        DrawPileAndDiscardPile deck = new DrawPileAndDiscardPile(new Random(3));
        while (deck.draw() != null) { /* drain */ }
        assertNull(deck.draw());
    }

    @Test
    void returnToBottomPutsCardAtBottomOfDrawPile() {
        DrawPileAndDiscardPile deck = new DrawPileAndDiscardPile(new Random(4));
        MoneyCards marker = new MoneyCards(10);
        deck.returnToBottom(marker);
        // The bottom is the first element. Drain until we find it and assert
        // we had to draw everything else first.
        int remaining = deck.drawPileSize();
        for (int i = 0; i < remaining - 1; i++) {
            assertTrue(deck.draw() != marker);
        }
        assertEquals(marker, deck.draw());
    }
}
