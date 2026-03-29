package 基础类;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<Card> PropertyCards;
    private ArrayList<Card> BankCards;
    private boolean isOnTurn;
    private DrawCards drawCards;
    private int UseCardTimes;
    private ArrayList<Player> Enemy;

    public Player(DrawCards drawCards) {
        Enemy = new ArrayList<>();
        HandCards = new ArrayList<>();
        PropertyCards = new ArrayList<>();
        BankCards = new ArrayList<>();
        this.isOnTurn = false;
        this.drawCards = drawCards;
        this.UseCardTimes = 0;
    }//创建玩家时 创建各种列表 手上的 地产 钱

    private void takeCard(int number) {
        for (int i = 0; i < number; i++) {
            if (drawCards.getDrawCards().isEmpty()) {
                drawCards.update();
                i-=1;
                continue;
            }
            HandCards.add(drawCards.getDrawCards().getFirst());
        }
    }//抓牌 抓几张 参数写几 牌不够了 带洗牌功能

    private void putMoneyCard(Card card) {
        HandCards.remove(card);
        BankCards.add(card);
    }//用钱 对应规则A

    private void putPropertyCard(PropertiesCards card) {
        HandCards.remove(card);
        PropertyCards.add(card);
    }//放地产 对应规则B

    private void putActionCard(ActionCards card) {
        HandCards.remove(card);
        switch (card.getActionCardType()) {
            case PASS_GO:
                takeCard(2);
                break;
        }
    }//对应规则C 行动卡

    public ArrayList<Card> getHandCards() {return HandCards;}

    public ArrayList<Card> getPropertyCards() {return PropertyCards;}

    public ArrayList<Card> getBankCards() {return BankCards;}

    public DrawCards getDrawCards() {return drawCards;}

    public ArrayList<Player> getEnemy() {return Enemy;}

    private boolean checkIfWin(ArrayList<Card> cards) {
        for (int i = 0; i < PropertyCards.size(); i++) {
            return true;
        }
        return false;
    }//判断游戏胜利 每个玩家每一个行动 触发检查胜利

    public boolean isOnTurn() {
        return isOnTurn;
    }

    public void setOnTurn(boolean onTurn) {
        isOnTurn = onTurn;
    }

    public int getUseCardTimes() {
        return UseCardTimes;
    }

    public void setUseCardTimes(int useCardTimes) {
        UseCardTimes = useCardTimes;
    }

    public void OnTurn(){
        isOnTurn = true;
        for (int i = 0; i < HandCards.size(); i++) {
            //需要GUI同学的画图
            for (int j = 0; j < 3; j++) {
                /* if (listener判断){
                    //使用卡牌
                    //判断是否合法
                }
            }
            if(checkIfWin(PropertyCards)){
                drawCards.update();
                //主菜单
            }*/
            }
        }
        isOnTurn = false;
    }
}
