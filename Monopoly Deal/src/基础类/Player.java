package 基础类;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<Card> PropertyCards;
    private ArrayList<Card> BankCards;
    private boolean onTurn;

    public Player() {
        HandCards = new ArrayList<>();
        PropertyCards = new ArrayList<>();
        BankCards = new ArrayList<>();
        onTurn = false;
    }//创建玩家时 创建各种列表 手上的 地产 钱

    private void takeCard(Card card) {
        HandCards.add(card);
    }//抓牌

    private void putMoneyCard(Card card) {
        BankCards.add(card);
    }//用钱 对应规则A

    private void putPropertyCard(Card card) {
        PropertyCards.add(card);
    }

    private void putActionCard(Card card) {

    }

    private boolean checkWin(Card card) {
        return true;
    }//判断游戏胜利 每个玩家每一个行动 触发检查胜利
}
