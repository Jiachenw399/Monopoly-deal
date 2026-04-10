package model;

public enum PropertiesCardsType {
    //深蓝 2 橘色 3 黑色4 红3 深绿3 棕色2 粉色3 浅蓝3 浅绿2 黄3
    DARK_BLUE(4),
    ORANGE(2),
    BLACK(2),
    RED(3),
    DARK_GREEN(4),
    BROWN(1),
    PINK(2),
    LIGHT_BLUE(1),
    LIGHT_GREEN(2),
    YELLOW(3),
    WILD_CARDS_WITH_PINK_AND_ORANGE(2),
    WILD_CARDS_WITH_RED_AND_YELLOW(3),
    WILD_CARDS_WITH_BLACK_AND_DARK_GREEN(4),
    WILD_CARDS_WITH_BLACK_AND_LIGHT_BLUE(4),
    WILD_CARDS_WITH_BLACK_AND_LIGHT_GREEN(2),
    WILD_CARDS_WITH_LIGHT_BLUE_AND_BROWN(1),
    WILD_CARDS_WITH_DARK_BLUE_AND_DARK_GREEN(4),//两种颜色之一的万能牌 随时可以变颜色
    //红黄2 橙粉2 浅蓝棕1 黑深绿1 黑浅蓝1 黑浅绿1 深蓝深绿1

    WILD_CARDS_WITH_MULTIPLE_COLOR(0);//2
    ;
    private int ColorValue;

    PropertiesCardsType(int value) {
        this.ColorValue = value;
    }

    public int getColorValue() {
        return ColorValue;
    }

    public void setColorValue(int colorValue) {
        ColorValue = colorValue;
    }
}
