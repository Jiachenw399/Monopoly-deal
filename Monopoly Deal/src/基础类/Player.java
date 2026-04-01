package 基础类;

import java.util.ArrayList;


public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<PropertiesCards> PropertyCards;
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
            case DEBT_COLLECTOR:
                //takeMoney(5,);
        }
    }//对应规则C 行动卡

    public ArrayList<Card> getHandCards() {return HandCards;}

    public ArrayList<PropertiesCards> getPropertyCards() {return PropertyCards;}

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

    public void OnTurn() {
        // 抽牌阶段
        if (HandCards.isEmpty()) {
            takeCard(5);
        } else {
            takeCard(2);
        }

        isOnTurn = true;
        UseCardTimes = 0;  // 重置本回合打牌计数

        // 打牌阶段：最多打出3张卡（依赖GUI Listener选择）
        while (UseCardTimes < 3 && !HandCards.isEmpty()) {
            // TODO: GUI通过GameListener传入玩家选择的卡牌
            // Card selectedCard = listener.getSelectedCard();

        /*
        if (selectedCard != null) {
            if (selectedCard instanceof MoneyCards) {
                putMoneyCard(selectedCard);
            } else if (selectedCard instanceof PropertiesCards) {
                putPropertyCard((PropertiesCards) selectedCard);
            } else if (selectedCard instanceof ActionCards) {
                putActionCard((ActionCards) selectedCard);
            }

            // 打牌后立即检查胜利
            if (checkIfWin(PropertyCards)) {
                // 可在此通知Game结束游戏
                break;
            }
        } else {
            break;  // 玩家选择结束回合
        }
        */
        }

        // 回合结束：手牌超过7张需弃牌（规则要求）
        while (HandCards.size() > 7) {
            // TODO: GUI选择要弃的牌
            // Card discard = ...;
            // HandCards.remove(discard);
            // drawCards.getDiscardPile().add(discard);
        }

        isOnTurn = false;
        UseCardTimes = 0;
    }
}
