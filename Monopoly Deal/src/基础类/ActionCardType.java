package 基础类;

public enum ActionCardType {
    SLY_DEAL,//偷一个地产 归你

    DEAL_BREAKER,//直接偷一套地

    DEBT_COLLECTOR,//随便找一个人拿五块

    RENT_WITH_TWO_COLOR,//对所有人使用 给你在那两个颜色中选择的任意一个颜色地的钱
    RENT_WITH_MULTIPLE_COLOR,//对一个人使用 给你选择的任意一个颜色地的钱
    DOUBLE_THE_RENT,//和以上两个rent之一一起使用 double

    HOUSE,//在全色地产上使用 增加租金 有了房子 才能有酒店
    HOTEL,

    FORCED_DEAL,//交换地产
    JUST_SAY_NO,//无懈可击 任意时候 任意玩家 对你出行动卡 让他失效 可以反无懈
    BIRTHDAY,//所有人一人给你两块

    PASS_GO,//无中生有 摸两张牌
    WILD_CARDS_WITH_TWO_COLOR,//两种颜色之一的万能牌 随时可以变颜色
    WILD_CARDS_WITH_MULTIPLE_COLOR;//所有颜色之一的万能牌 可以随时变颜色 唯一一张卡 没有价值
}
