package model;

public enum ActionCardType {
    //做逻辑的同学 务必仔细看各个卡牌规则
    SLY_DEAL(3),//偷一个地产 归你 放在地产区 3 集齐了的不能偷
    DEAL_BREAKER(5),//直接偷一套地 放在地产区 一套地都齐的时候 也可以偷 2
    FORCED_DEAL(3),//交换你和其他一个人的地产 3

    DEBT_COLLECTOR(3),//随便找一个人拿五块 放手牌 3
    RENT_WITH_RED_AND_YELLOW(1),
    RENT_WITH_ORANGE_AND_PINK(1),
    RENT_WITH_BROWN_AND_LIGHT_BLUE(1),
    RENT_WITH_BLACK_AND_LIGHT_GREEN(1),
    RENT_WITH_DARK_BLUE_AND_DARK_GREEN(1),//对所有人使用 给你在那两个颜色中选择的任意一个颜色地的钱 放手牌
    //红黄2 橙粉2 浅蓝棕2 浅绿黑2 深蓝深绿2
    RENT_WITH_MULTIPLE_COLOR(3),//对一个人使用 给你选择的任意一个颜色地的钱 放手牌 3
    DOUBLE_THE_RENT(1),//和以上两个rent之一一起使用 double 2

    HOUSE(3),//在全色地产上使用 增加租金 有了房子 才能有酒店 3
    HOTEL(4),//2


    JUST_SAY_NO(4),//无懈可击 任意时候 任意玩家 对你出行动卡 只能对你 让他失效 可以反无懈 3
    BIRTHDAY(2),//所有人一人给你两块 3

    PASS_GO(1);//无中生有 摸两张牌 10

    private int TypeValue;

    ActionCardType(int TypeValue) {
        this.TypeValue = TypeValue;
    }

    public int getTypeValue() {
        return TypeValue;
    }

    public void setTypeValue(int typeValue) {
        TypeValue = typeValue;
    }
}
