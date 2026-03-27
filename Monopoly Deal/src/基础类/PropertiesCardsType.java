package 基础类;

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
    YELLOW(3)
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
