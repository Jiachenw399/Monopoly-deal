package model;

/**
 * Money card. Per the game rules only three denominations exist: 1M, 4M, 10M.
 * Money cards are placed into a player's bank when played.
 */
public class MoneyCards extends Card {
    public static final int[] DENOMINATIONS = {1, 4, 10};

    public MoneyCards(int value) {
        if (!isValidDenomination(value)) {
            throw new IllegalArgumentException(
                    "Money denomination must be 1, 4 or 10 but was " + value);
        }
        this.value = value;
    }

    public static boolean isValidDenomination(int v) {
        for (int d : DENOMINATIONS) {
            if (d == v) return true;
        }
        return false;
    }

    @Override
    public String getDisplay() {
        return "$" + value + "M";
    }

    @Override
    public String getCategory() {
        return "MONEY";
    }
}
