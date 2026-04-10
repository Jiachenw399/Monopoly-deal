package model;

import java.util.Set;

public enum PropertiesCardsType {

    // ===== 普通颜色牌 =====
    DARK_BLUE(4, Set.of(PropertyColor.DARK_BLUE)),
    ORANGE(2, Set.of(PropertyColor.ORANGE)),
    BLACK(2, Set.of(PropertyColor.BLACK)),
    RED(3, Set.of(PropertyColor.RED)),
    DARK_GREEN(4, Set.of(PropertyColor.DARK_GREEN)),
    BROWN(1, Set.of(PropertyColor.BROWN)),
    PINK(2, Set.of(PropertyColor.PINK)),
    LIGHT_BLUE(1, Set.of(PropertyColor.LIGHT_BLUE)),
    LIGHT_GREEN(2, Set.of(PropertyColor.LIGHT_GREEN)),
    YELLOW(3, Set.of(PropertyColor.YELLOW)),

    // ===== 万能牌 =====
    WILD_PINK_ORANGE(2, Set.of(PropertyColor.PINK, PropertyColor.ORANGE)),
    WILD_RED_YELLOW(3, Set.of(PropertyColor.RED, PropertyColor.YELLOW)),
    WILD_BLACK_DARK_GREEN(4, Set.of(PropertyColor.BLACK, PropertyColor.DARK_GREEN)),
    WILD_BLACK_LIGHT_BLUE(4, Set.of(PropertyColor.BLACK, PropertyColor.LIGHT_BLUE)),
    WILD_BLACK_LIGHT_GREEN(2, Set.of(PropertyColor.BLACK, PropertyColor.LIGHT_GREEN)),
    WILD_LIGHT_BLUE_BROWN(1, Set.of(PropertyColor.LIGHT_BLUE, PropertyColor.BROWN)),
    WILD_DARK_BLUE_DARK_GREEN(4, Set.of(PropertyColor.DARK_BLUE, PropertyColor.DARK_GREEN)),

    // 🌈 全能万能牌
    WILD_ALL(0, Set.of(PropertyColor.values()));

    private final int value;
    private final Set<PropertyColor> colors;

    PropertiesCardsType(int value, Set<PropertyColor> colors) {
        this.value = value;
        this.colors = colors;
    }

    public int getValue() {
        return value;
    }

    public Set<PropertyColor> getColors() {
        return colors;
    }

    // 是否可以当某种颜色
    public boolean canBe(PropertyColor color) {
        return colors.contains(color);
    }

}