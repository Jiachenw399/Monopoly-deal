package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class PlayerTest {

    private DrawPileAndDiscardPile freshDeck() {
        return new DrawPileAndDiscardPile();
    }

    @Test
    void drawAddsCardsToHand() {
        Player p = new Player("A");
        DrawPileAndDiscardPile deck = freshDeck();
        p.drawFrom(deck, 5);
        assertEquals(5, p.getHand().size());
    }

    @Test
    void playMoneyMovesCardToBank() {
        Player p = new Player("A");
        MoneyCards c = new MoneyCards(4);
        p.getHand().add(c);
        p.playMoney(c);
        assertEquals(0, p.getHand().size());
        assertEquals(1, p.getBank().size());
        assertEquals(4, p.bankTotal());
    }

    @Test
    void playPropertyFilesByColor() {
        Player p = new Player("A");
        PropertiesCards prop = new PropertiesCards(PropertyColor.RED);
        p.getHand().add(prop);
        p.playProperty(prop);
        assertEquals(1, p.propertyCount(PropertyColor.RED));
        assertEquals(0, p.propertyCount(PropertyColor.BLACK));
    }

    @Test
    void playingCardNotInHandFails() {
        Player p = new Player("A");
        MoneyCards c = new MoneyCards(1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> p.playMoney(c));
        assertTrue(ex.getMessage() != null);
    }

    @Test
    void minSufficientPaymentPicksSmallestSubsetAtLeastAmount() {
        Player p = new Player("A");
        // bank = [1,1,4,10], demand = 3 -> should pay 4 (single card, smallest >=3)
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(4));
        p.getBank().add(new MoneyCards(10));
        List<MoneyCards> payment = p.computeMinSufficientPayment(3);
        int sum = payment.stream().mapToInt(MoneyCards::getValue).sum();
        assertEquals(4, sum);
        assertEquals(1, payment.size());
    }

    @Test
    void minSufficientPaymentUsesCombinationWhenCheaper() {
        Player p = new Player("A");
        // bank = [1,1,1,10], demand = 3 -> pay 1+1+1=3 (not the 10)
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(10));
        List<MoneyCards> payment = p.computeMinSufficientPayment(3);
        int sum = payment.stream().mapToInt(MoneyCards::getValue).sum();
        assertEquals(3, sum);
        assertEquals(3, payment.size());
    }

    @Test
    void minSufficientPaymentReturnsWholeBankWhenInsufficient() {
        Player p = new Player("A");
        p.getBank().add(new MoneyCards(1));
        p.getBank().add(new MoneyCards(1));
        List<MoneyCards> payment = p.computeMinSufficientPayment(10);
        assertEquals(2, payment.size());
    }

    @Test
    void payTransfersCardsToReceiver() {
        Player payer = new Player("P");
        Player receiver = new Player("R");
        MoneyCards c = new MoneyCards(4);
        payer.getBank().add(c);
        payer.pay(List.of(c), receiver);
        assertEquals(0, payer.getBank().size());
        assertEquals(1, receiver.getBank().size());
    }

    @Test
    void hasWonRequiresThreeCompleteSets() {
        Player p = new Player("A");
        // BROWN set = 2 cards
        p.getProperties().get(PropertyColor.BROWN).add(new PropertiesCards(PropertyColor.BROWN));
        p.getProperties().get(PropertyColor.BROWN).add(new PropertiesCards(PropertyColor.BROWN));
        // DARK_BLUE set = 2
        p.getProperties().get(PropertyColor.DARK_BLUE).add(new PropertiesCards(PropertyColor.DARK_BLUE));
        p.getProperties().get(PropertyColor.DARK_BLUE).add(new PropertiesCards(PropertyColor.DARK_BLUE));
        assertFalse(p.hasWon());
        // LIGHT_GREEN set = 2
        p.getProperties().get(PropertyColor.LIGHT_GREEN).add(new PropertiesCards(PropertyColor.LIGHT_GREEN));
        p.getProperties().get(PropertyColor.LIGHT_GREEN).add(new PropertiesCards(PropertyColor.LIGHT_GREEN));
        assertTrue(p.hasWon());
        assertEquals(3, p.completeSetCount());
    }
}
