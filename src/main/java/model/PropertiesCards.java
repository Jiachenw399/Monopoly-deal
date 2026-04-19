package model;

/**
 * A property card in a single color. Multi-color wild cards were removed to
 * match the simplified rules: every property belongs to exactly one color.
 * Face value is a flat 2M, which is what the card is worth when it ends up
 * in a bank pile as payment (properties themselves are not used to pay in
 * the simplified rules, this value only matters for deck totals / display).
 */
public class PropertiesCards extends Card {
    public static final int FACE_VALUE = 2;

    private final PropertyColor color;

    public PropertiesCards(PropertyColor color) {
        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }
        this.color = color;
        this.value = FACE_VALUE;
    }

    public PropertyColor getColor() {
        return color;
    }

    @Override
    public String getDisplay() {
        return color.getDisplayName();
    }

    @Override
    public String getCategory() {
        return "PROPERTY";
    }
}
