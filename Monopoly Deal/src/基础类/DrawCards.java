package 基础类;

import javax.imageio.metadata.IIOMetadataFormatImpl;
import java.util.ArrayList;

public class DrawCards {
    private ArrayList<Card> DrawCards;
    public DrawCards() {
        DrawCards = new ArrayList<>();
        //创建对象时 更新
    }

    public void xipai(){
        if(DrawCards.size()<5){
            //更新
        }
    }

    public ArrayList<Card> getDrawCards() {
        return DrawCards;
    }

    public void update(){
        DrawCards.clear();
        addMoneyCards();
        addActionCards();
        addPropertiesCards();
        //游戏结束时 更新牌堆
    }

    private void addMoneyCards(){
        for (int i = 0; i < 6; i++) {
            MoneyCards moneyCards = new MoneyCards(1);
            DrawCards.add(moneyCards);
        }
        for (int i = 0; i < 5; i++) {
            MoneyCards moneyCards = new MoneyCards(2);
            DrawCards.add(moneyCards);
        }
        for (int i = 0; i < 3; i++) {
            MoneyCards moneyCards = new MoneyCards(3);
            DrawCards.add(moneyCards);
            MoneyCards moneyCards1 = new MoneyCards(4);
            DrawCards.add(moneyCards1);
        }
        for (int i = 0; i < 2; i++) {
            MoneyCards moneyCards = new MoneyCards(5);
            DrawCards.add(moneyCards);
        }
        MoneyCards moneyCards1 = new MoneyCards(6);
        DrawCards.add(moneyCards1);
    }

    private void addActionCards(){
        for (int i = 0; i < 3; i++) {
            ActionCards ac = new ActionCards(ActionCardType.SLY_DEAL);
            DrawCards.add(ac);
            ActionCards ac1 = new ActionCards(ActionCardType.DEBT_COLLECTOR);
            DrawCards.add(ac1);
            ActionCards ac2 = new ActionCards(ActionCardType.RENT_WITH_MULTIPLE_COLOR);
            DrawCards.add(ac2);
            ActionCards ac3 = new ActionCards(ActionCardType.HOUSE);
            DrawCards.add(ac3);
            ActionCards ac4 = new ActionCards(ActionCardType.FORCED_DEAL);
            DrawCards.add(ac4);
            ActionCards ac5 = new ActionCards(ActionCardType.BIRTHDAY);
            DrawCards.add(ac5);
            ActionCards ac6 = new ActionCards(ActionCardType.JUST_SAY_NO);
            DrawCards.add(ac6);
        }

        for (int i = 0; i < 2; i++) {
            ActionCards ac = new ActionCards(ActionCardType.DOUBLE_THE_RENT);
            DrawCards.add(ac);
            ActionCards ac1 = new ActionCards(ActionCardType.RENT_WITH_MULTIPLE_COLOR);
            DrawCards.add(ac1);
            ActionCards ac2 = new ActionCards(ActionCardType.HOTEL);
            DrawCards.add(ac2);
            ActionCards ac3 = new ActionCards(ActionCardType.DEAL_BREAKER);
            DrawCards.add(ac3);

        }

    }

    private void addPropertiesCards(){
        //深蓝 2 橘色 3 黑色4 红3 深绿3 棕色2 粉色3 浅蓝3 浅绿2 黄3
        for (int i = 0; i < 2; i++) {
            PropertiesCards ps = new PropertiesCards(PropertiesCardsType.DARK_BLUE);
            DrawCards.add(ps);
            PropertiesCards ps1 = new PropertiesCards(PropertiesCardsType.BROWN);
            DrawCards.add(ps1);
            PropertiesCards ps2 = new PropertiesCards(PropertiesCardsType.LIGHT_GREEN);
            DrawCards.add(ps2);
        }
        for (int i = 0; i < 3; i++) {
            PropertiesCards ps = new PropertiesCards(PropertiesCardsType.ORANGE);
            DrawCards.add(ps);
            PropertiesCards ps1 = new PropertiesCards(PropertiesCardsType.RED);
            DrawCards.add(ps1);
            PropertiesCards ps2 = new PropertiesCards(PropertiesCardsType.DARK_GREEN);
            DrawCards.add(ps2);
            PropertiesCards ps3 = new PropertiesCards(PropertiesCardsType.LIGHT_BLUE);
            DrawCards.add(ps3);
            PropertiesCards ps4 = new PropertiesCards(PropertiesCardsType.YELLOW);
            DrawCards.add(ps4);
            PropertiesCards ps5 = new PropertiesCards(PropertiesCardsType.PINK);
            DrawCards.add(ps5);
        }
        for (int i = 0; i < 4; i++) {
            PropertiesCards ps = new PropertiesCards(PropertiesCardsType.BLACK);
            DrawCards.add(ps);
        }


    }
}
