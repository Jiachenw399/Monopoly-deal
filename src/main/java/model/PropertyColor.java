package model;

/**
 * The ten property colors supported by the game. Each color has a fixed
 * set size (the number of cards needed to complete a full set) and a
 * display hex color used by the GUI layer.
 */
public enum PropertyColor {
    DARK_BLUE(2, "#1a4b8c", "Dark Blue"),
    ORANGE(3, "#e67e22", "Orange"),
    BLACK(4, "#2c3e50", "Black"),
    RED(3, "#c0392b", "Red"),
    DARK_GREEN(3, "#1e824c", "Dark Green"),
    BROWN(2, "#8b5a2b", "Brown"),
    PINK(3, "#e91e63", "Pink"),
    LIGHT_BLUE(3, "#5dade2", "Light Blue"),
    LIGHT_GREEN(2, "#52be80", "Light Green"),
    YELLOW(3, "#f1c40f", "Yellow");

    private final int setSize;
    private final String hex;
    private final String displayName;

    PropertyColor(int setSize, String hex, String displayName) {
        this.setSize = setSize;
        this.hex = hex;
        this.displayName = displayName;
    }

    public int getSetSize() {
        return setSize;
    }

    public String getHex() {
        return hex;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Backwards compatible alias used by older callers / tests. */
    public int getAmountToCompleteSet() {
        return setSize;
    }
}
