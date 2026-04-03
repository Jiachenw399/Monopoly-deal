package 基础类;

public class PropertiesCards extends Card {
    private final PropertiesCardsType type;

    public PropertiesCardsType getType() {
        return type;
    }
    //深蓝 2 橘色 3 黑色4 红3 深绿3 棕色2 粉色3 浅蓝3 浅绿2 黄3
    //地产 功能实现
    public PropertiesCards(PropertiesCardsType colortype) {
        type = colortype;
        value = colortype.getColorValue();
    }
}
