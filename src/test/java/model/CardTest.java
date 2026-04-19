package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void moneyAcceptsOnlyValidDenominations() {
        assertTrue(MoneyCards.isValidDenomination(1));
        assertTrue(MoneyCards.isValidDenomination(4));
        assertTrue(MoneyCards.isValidDenomination(10));
        assertFalse(MoneyCards.isValidDenomination(2));
        assertFalse(MoneyCards.isValidDenomination(0));
    }

    @Test
    void moneyRejectsInvalidDenominations() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> new MoneyCards(3));
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> new MoneyCards(99));
        assertTrue(e1 != null && e2 != null);
    }

    @Test
    void moneyExposesCategoryAndValue() {
        MoneyCards m = new MoneyCards(4);
        assertEquals(4, m.getValue());
        assertEquals("MONEY", m.getCategory());
        assertEquals("$4M", m.getDisplay());
    }

    @Test
    void propertyCardStoresColor() {
        PropertiesCards p = new PropertiesCards(PropertyColor.RED);
        assertEquals(PropertyColor.RED, p.getColor());
        assertEquals("PROPERTY", p.getCategory());
    }

    @Test
    void rentCardStoresColorAndCategory() {
        ActionCards r = new ActionCards(PropertyColor.YELLOW);
        assertEquals(PropertyColor.YELLOW, r.getColor());
        assertEquals("RENT", r.getCategory());
        assertTrue(r.getDisplay().contains("Yellow"));
    }

    @Test
    void propertyColorSetSizesArePositive() {
        for (PropertyColor c : PropertyColor.values()) {
            assertTrue(c.getSetSize() > 0, c + " must have positive set size");
        }
    }
}
