package 基础类;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<Card> PropertyCards;
    private ArrayList<Card> BankCards;
    private boolean onTurn;
    private DrawCards drawCards;

    public Player(DrawCards drawCards) {
        HandCards = new ArrayList<>();
        PropertyCards = new ArrayList<>();
        BankCards = new ArrayList<>();
        onTurn = false;
        this.drawCards = drawCards;
    }//创建玩家时 创建各种列表 手上的 地产 钱

    private void takeCard(int number) {
        for (int i = 0; i < number; i++) {
            if (drawCards.getDrawCards().isEmpty()) {
                drawCards.xipai();
            }
            HandCards.add(drawCards.getDrawCards().getFirst());
        }
    }//抓牌 抓几张 参数写几 带洗牌功能

    private void putMoneyCard(Card card) {
        HandCards.remove(card);
        BankCards.add(card);
    }//用钱 对应规则A

    private void putPropertyCard(Card card) {
        HandCards.remove(card);
        PropertyCards.add(card);
    }//放地产 对应规则B

    private void putActionCard(Card card) {
        HandCards.remove(card);
    }//对应规则C 行动卡

    private boolean checkWin(Card card) {
        for (int i = 0; i < PropertyCards.size(); i++) {
            return true;
        }
        return false;
    }//判断游戏胜利 每个玩家每一个行动 触发检查胜利
}
