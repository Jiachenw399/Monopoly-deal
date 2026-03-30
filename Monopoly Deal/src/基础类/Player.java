package 基础类;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<Card> PropertyCards;
    private ArrayList<Card> BankCards;
    private boolean isOnTurn;
    private DrawPileAndDiscardPile drawCards;
    private int UseCardTimes;
    private ArrayList<Player> Enemy;

    public Player(DrawPileAndDiscardPile drawCards) {
        Enemy = new ArrayList<>();
        HandCards = new ArrayList<>();
        PropertyCards = new ArrayList<>();
        BankCards = new ArrayList<>();
        this.isOnTurn = false;
        this.drawCards = drawCards;
        this.UseCardTimes = 0;
    }//创建玩家时 创建各种列表 手上的 地产 钱

    public void takeCard(int number) {
        for (int i = 0; i < number; i++) {
            if (drawCards.getDrawPile().isEmpty()) {
                drawCards.update();
                i-=1;
                continue;
            }
            HandCards.add(drawCards.getDrawPile().getFirst());
        }
    }//抓牌 抓几张 参数写几 牌不够了 带洗牌功能

    public void takeMoney(int number,Player player) {
        for (int i = 0; i < player.getBankCards().size(); i++) {
            //想办法实现不找零也不补齐
        }
    }

    public void takeMoney(int number,ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            takeMoney(number,players.get(i));
        }
    }

    private void putMoneyCard(Card card) {
        if(card.getClass().equals(PropertiesCards.class)){
            return;
        }
        HandCards.remove(card);
        BankCards.add(card);
        UseCardTimes++;
    }//用钱 对应规则A

    private void putPropertyCard(PropertiesCards card) {
        HandCards.remove(card);
        PropertyCards.add(card);
        UseCardTimes++;
    }//放地产 对应规则B

    private void putActionCard(ActionCards card) {
        HandCards.remove(card);
        drawCards.getDiscardPile().add(card);
        switch (card.getActionCardType()) {
            case PASS_GO:
                takeCard(2);
                break;
            case BIRTHDAY:
                takeMoney(2,Enemy);
                break;
        }
    }//对应规则C 行动卡

    public ArrayList<Card> getHandCards() {return HandCards;}

    public ArrayList<Card> getPropertyCards() {return PropertyCards;}

    public ArrayList<Card> getBankCards() {return BankCards;}

    public DrawPileAndDiscardPile getDrawCards() {return drawCards;}

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
        if(HandCards.isEmpty()){
            takeCard(5);
        }else{
            takeCard(3);
        }
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
