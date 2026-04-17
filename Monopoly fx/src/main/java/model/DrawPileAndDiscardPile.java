package model;

import java.util.ArrayList;
import java.util.Collections;

//牌堆 功能完全实现 辛苦修改bug的同学 进行bug测试
public class DrawPileAndDiscardPile {
    private ArrayList<Card> DrawPile;
    private ArrayList<Card> DiscardPile;
    public DrawPileAndDiscardPile() {
        DrawPile = new ArrayList<>();
        DiscardPile = new ArrayList<>();
        addMoneyCards();
        addActionCards();
        addPropertiesCards();
        shuffleDrawCards();
        //创建对象时 更新
    }

    public void shuffleDrawCards() {
        Collections.shuffle(DrawPile);
    }

    public void update(){
        DrawPile.clear();
        addMoneyCards();
        addActionCards();
        addPropertiesCards();
        shuffleDrawCards();
        //游戏结束时 更新牌堆
    }

    public void xipai(){
        if(DrawPile.isEmpty()){
            DrawPile.addAll(DiscardPile);
            shuffleDrawCards();
        }
    }

    public ArrayList<Card> getDrawPile() {return DrawPile;}

    public ArrayList<Card> getDiscardPile() {return DiscardPile;}

    private void addMoneyCards(){
        int[] moneyValues = {
                1,1,1,1,1,1,  // 6张1
                2,2,2,2,2,    // 5张2
                3,4,3,4,3,4,  // 3张3 + 3张4
                5,5,          // 2张5
                6};           // 1张6
        // 循环添加所有卡牌
        for (int value : moneyValues) {
            DrawPile.add(new MoneyCards(value));
        }
    }

    private void addActionCards(){
        addActionCards(ActionCardType.SLY_DEAL, 3);
        addActionCards(ActionCardType.RENT_WITH_MULTIPLE_COLOR, 3);
        addActionCards(ActionCardType.HOUSE, 3);
        addActionCards(ActionCardType.FORCED_DEAL, 3);
        addActionCards(ActionCardType.BIRTHDAY, 3);
        addActionCards(ActionCardType.JUST_SAY_NO, 3);
        addActionCards(ActionCardType.DEBT_COLLECTOR, 3);

        addActionCards(ActionCardType.DOUBLE_THE_RENT, 2);
        addActionCards(ActionCardType.HOTEL, 2);
        addActionCards(ActionCardType.DEAL_BREAKER, 2);
        addActionCards(ActionCardType.RENT_WITH_DARK_BLUE_AND_DARK_GREEN, 2);
        addActionCards(ActionCardType.RENT_WITH_BROWN_AND_LIGHT_BLUE, 2);
        addActionCards(ActionCardType.RENT_WITH_BLACK_AND_LIGHT_GREEN, 2);
        addActionCards(ActionCardType.RENT_WITH_RED_AND_YELLOW, 2);
        addActionCards(ActionCardType.RENT_WITH_ORANGE_AND_PINK, 2);

        addActionCards(ActionCardType.PASS_GO, 10);
    }

    private void addPropertiesCards() {
        addPropertiesCards(PropertiesCardsType.DARK_BLUE, 2);
        addPropertiesCards(PropertiesCardsType.ORANGE, 3);
        addPropertiesCards(PropertiesCardsType.BLACK, 4);
        addPropertiesCards(PropertiesCardsType.RED, 3);
        addPropertiesCards(PropertiesCardsType.DARK_GREEN, 3);
        addPropertiesCards(PropertiesCardsType.BROWN, 2);
        addPropertiesCards(PropertiesCardsType.PINK, 3);
        addPropertiesCards(PropertiesCardsType.LIGHT_BLUE, 3);
        addPropertiesCards(PropertiesCardsType.LIGHT_GREEN, 2);
        addPropertiesCards(PropertiesCardsType.YELLOW, 3);

        addPropertiesCards(PropertiesCardsType.WILD_PINK_ORANGE, 2);
        addPropertiesCards(PropertiesCardsType.WILD_RED_YELLOW, 2);
        addPropertiesCards(PropertiesCardsType.WILD_BLACK_DARK_GREEN, 1);
        addPropertiesCards(PropertiesCardsType.WILD_BLACK_LIGHT_BLUE, 1);
        addPropertiesCards(PropertiesCardsType.WILD_BLACK_LIGHT_GREEN, 1);
        addPropertiesCards(PropertiesCardsType.WILD_LIGHT_BLUE_BROWN, 1);
        addPropertiesCards(PropertiesCardsType.WILD_DARK_BLUE_DARK_GREEN, 1);
        addPropertiesCards(PropertiesCardsType.WILD_ALL, 2);
    }

    private void addActionCards(ActionCardType type, int amount) {
        for (int i = 0; i < amount; i++) {
            DrawPile.add(new ActionCards(type));
        }
    }

    private void addPropertiesCards(PropertiesCardsType type, int amount) {
        for (int i = 0; i < amount; i++) {
            DrawPile.add(new PropertiesCards(type));
        }
    }
}
