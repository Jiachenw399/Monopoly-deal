package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PropertiesCardsType {

    // ===== 普通颜色牌 =====
    DARK_BLUE(4, new ArrayList<>(List.of(PropertyColor.DARK_BLUE))),
    ORANGE(2, new ArrayList<>(List.of(PropertyColor.ORANGE))),
    BLACK(2, new ArrayList<>(List.of(PropertyColor.BLACK))),
    RED(3, new ArrayList<>(List.of(PropertyColor.RED))),
    DARK_GREEN(4, new ArrayList<>(List.of(PropertyColor.DARK_GREEN))),
    BROWN(1, new ArrayList<>(List.of(PropertyColor.BROWN))),
    PINK(2, new ArrayList<>(List.of(PropertyColor.PINK))),
    LIGHT_BLUE(1, new ArrayList<>(List.of(PropertyColor.LIGHT_BLUE))),
    LIGHT_GREEN(2, new ArrayList<>(List.of(PropertyColor.LIGHT_GREEN))),
    YELLOW(3, new ArrayList<>(List.of(PropertyColor.YELLOW))),

    // ===== 万能牌 =====
    WILD_PINK_ORANGE(2, new ArrayList<>(Arrays.asList(PropertyColor.PINK, PropertyColor.ORANGE))),
    WILD_RED_YELLOW(3, new ArrayList<>(Arrays.asList(PropertyColor.RED, PropertyColor.YELLOW))),
    WILD_BLACK_DARK_GREEN(4, new ArrayList<>(Arrays.asList(PropertyColor.BLACK, PropertyColor.DARK_GREEN))),
    WILD_BLACK_LIGHT_BLUE(4, new ArrayList<>(Arrays.asList(PropertyColor.BLACK, PropertyColor.LIGHT_BLUE))),
    WILD_BLACK_LIGHT_GREEN(2, new ArrayList<>(Arrays.asList(PropertyColor.BLACK, PropertyColor.LIGHT_GREEN))),
    WILD_LIGHT_BLUE_BROWN(1, new ArrayList<>(Arrays.asList(PropertyColor.LIGHT_BLUE, PropertyColor.BROWN))),
    WILD_DARK_BLUE_DARK_GREEN(4, new ArrayList<>(Arrays.asList(PropertyColor.DARK_BLUE, PropertyColor.DARK_GREEN))),

    // 🌈 全能万能牌
    WILD_ALL(0, new ArrayList<>(Arrays.asList(PropertyColor.values())));

    private final int value;
    private final ArrayList<PropertyColor> colors;

    PropertiesCardsType(int value, ArrayList<PropertyColor> colors) {
        this.value = value;
        this.colors = colors;
    }

    public int getValue() {
        return value;
    }

    public ArrayList<PropertyColor> getColors() {
        return colors;
    }

    // 是否可以当某种颜色
    public boolean canBe(PropertyColor color) {
        return colors.contains(color);
    }
}