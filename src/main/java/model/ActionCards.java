package model;

/**
 * Rent card. Targets one opponent and demands rent in the card's color.
 *
 * Preconditions enforced by {@code Game}:
 *  - the attacker must own at least one property of this color
 *  - the targeted opponent must also own at least one property of this color
 *
 * Rent amount = attacker.propertyCount(color) * {@link #RENT_PER_PROPERTY}.
 *
 * The class is still named {@code ActionCards} to avoid breaking the file
 * layout of the original project, but functionally it represents a rent card.
 */
public class ActionCards extends Card {
    public static final int RENT_PER_PROPERTY = 2;

    private final PropertyColor color;

    public ActionCards(PropertyColor color) {
        if (color == null) {
            throw new IllegalArgumentException("rent color must not be null");
        }
        this.color = color;
        this.value = 1;
    }

    public PropertyColor getColor() {
        return color;
    }

    @Override
    public String getDisplay() {
        return "Rent " + color.getDisplayName();
    }

    @Override
    public String getCategory() {
        return "RENT";
    }
}
