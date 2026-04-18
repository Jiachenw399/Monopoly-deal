package model;

public class PropertiesCards extends Card {
    private PropertyColor currentColor;
    private PropertiesCardsType type;

    public PropertiesCards(PropertiesCardsType type) {
        this.type = type;

        // 如果是普通牌（只有一种颜色）
        if (type.getColors().size() == 1) {
            this.currentColor = type.getColors().iterator().next();
        } else {
            // 万能牌先不指定颜色
            this.currentColor = null;
        }
    }

    public PropertiesCardsType getType() {
        return type;
    }

    public PropertyColor getCurrentColor() {
        return currentColor;
    }//用于记录地产卡现在的颜色 后续 胜利判断 偷牌 全都使用这个来进行判断

    public void setCurrentColor(PropertyColor currentColor) {
        this.currentColor = currentColor;
    }

    public void setType(PropertiesCardsType type) {
        this.type = type;
    }

    public int getValue() {
        return type.getValue(); // ✅ 直接从枚举拿
    }

    public boolean isWildCard() {
        return type.getColors().size() > 1;
    }
}
