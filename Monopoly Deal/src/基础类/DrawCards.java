package 基础类;

import java.util.ArrayList;
import java.util.Collections;

//牌堆 功能完全实现 辛苦修改bug的同学 进行bug测试
public class DrawCards {
    private ArrayList<Card> DrawCards;
    public DrawCards() {
        DrawCards = new ArrayList<>();
        addActionCards();
        addMoneyCards();
        addPropertiesCards();
        shuffleDrawCards();
        //创建对象时 更新
    }

    public void shuffleDrawCards() {
        Collections.shuffle(DrawCards);
    }

    public void update(){
        DrawCards.clear();
        addMoneyCards();
        addActionCards();
        addPropertiesCards();
        shuffleDrawCards();
        //游戏结束时 更新牌堆

    }

    public ArrayList<Card> getDrawCards() {return DrawCards;}

    private void addMoneyCards(){
        int[] moneyValues = {1,1,1,1,1,1,  // 6张1
                2,2,2,2,2,    // 5张2
                3,4,3,4,3,4,  // 3张3 + 3张4
                5,5,          // 2张5
                6};           // 1张6
        // 循环添加所有卡牌
        for (int value : moneyValues) {
            DrawCards.add(new MoneyCards(value));
        }
    }

    private void addActionCards(){
        int [] amount={
            3,3,3,3,3,3,3,
            2,2,2,2,2,2,2,2,2,2,2,2,
            10,
            1,1,1,1,1
        };

        ActionCardType [] actionCardType={
            ActionCardType.SLY_DEAL,
            ActionCardType.RENT_WITH_MULTIPLE_COLOR,
            ActionCardType.RENT_WITH_MULTIPLE_COLOR,
            ActionCardType.HOUSE,
            ActionCardType.FORCED_DEAL,
            ActionCardType.BIRTHDAY,
            ActionCardType.JUST_SAY_NO,

            ActionCardType.DOUBLE_THE_RENT,
            ActionCardType.RENT_WITH_MULTIPLE_COLOR,
            ActionCardType.HOTEL,
            ActionCardType.DEAL_BREAKER,
            ActionCardType.WILD_CARDS_WITH_MULTIPLE_COLOR,
            ActionCardType.WILD_CARDS_WITH_RED_AND_YELLOW,
            ActionCardType.RENT_WITH_DARK_BLUE_AND_DARK_GREEN,
            ActionCardType.RENT_WITH_BROWN_AND_LIGHT_BLUE,
            ActionCardType.RENT_WITH_BLACK_AND_LIGHT_GREEN,
            ActionCardType.RENT_WITH_RED_AND_YELLOW,
            ActionCardType.RENT_WITH_ORANGE_AND_PINK,
            ActionCardType.WILD_CARDS_WITH_PINK_AND_ORANGE,

            ActionCardType.PASS_GO,

            ActionCardType.WILD_CARDS_WITH_BLACK_AND_DARK_GREEN,
            ActionCardType.WILD_CARDS_WITH_BLACK_AND_LIGHT_BLUE,
            ActionCardType.WILD_CARDS_WITH_BLACK_AND_LIGHT_GREEN,
            ActionCardType.WILD_CARDS_WITH_LIGHT_BLUE_AND_BROWN,
            ActionCardType.WILD_CARDS_WITH_DARK_BLUE_AND_DARK_GREEN,
        };

        for (int i = 0; i < actionCardType.length; i++) {
            for (int j = 0; j < amount[i]; j++) {
                ActionCards ac = new ActionCards(actionCardType[i]);
                DrawCards.add(ac);
            }
        }
    }

    private void addPropertiesCards(){
        PropertiesCardsType [] types = new PropertiesCardsType[]{
            PropertiesCardsType.DARK_BLUE,
            PropertiesCardsType.BROWN,
            PropertiesCardsType.LIGHT_GREEN,

            PropertiesCardsType.ORANGE,
            PropertiesCardsType.PINK,
            PropertiesCardsType.YELLOW,
            PropertiesCardsType.DARK_GREEN,
            PropertiesCardsType.LIGHT_BLUE,
            PropertiesCardsType.RED,

            PropertiesCardsType.BLACK,

        };
        int [] amount = new int[]{
            2,2,2,
            3,3,3,3,3,3,
            4
        };
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < amount[i]; j++) {
                PropertiesCards ps = new PropertiesCards(types[i]);
                DrawCards.add(ps);
            }
        }
        //深蓝 2 橘色 3 黑色4 红3 深绿3 棕色2 粉色3 浅蓝3 浅绿2 黄3
    }
}
