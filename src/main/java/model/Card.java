package model;

/**
 * Base type for every card in the game. A card carries a monetary value
 * and can describe itself for UI / logging purposes.
 */
public abstract class Card {
    protected int value;

    public int getValue() {
        return value;
    }

    /** Short human readable label, e.g. "$4M" or "Rent RED". */
    public abstract String getDisplay();

    /** Category used by game rules: MONEY / PROPERTY / RENT. */
    public abstract String getCategory();

    @Override
    public String toString() {
        return getDisplay();
    }
}
