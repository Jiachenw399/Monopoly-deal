package 基础类;

import java.util.ArrayList;
import java.util.List;


public class Player {
    private ArrayList<Card> HandCards;
    private ArrayList<PropertiesCards> PropertyCards;
    private ArrayList<Card> BankCards;
    private boolean isOnTurn;
    private DrawPileAndDiscardPile drawCardsAndDiscardPile;
    private int UseCardTimes;
    private ArrayList<Player> Enemy;
    static int bestSum = Integer.MAX_VALUE;
    static List<Integer> best = new ArrayList<>();

    public void setHandCards(ArrayList<Card> handCards) {
        HandCards = handCards;
    }

    public void setPropertyCards(ArrayList<PropertiesCards> propertyCards) {
        PropertyCards = propertyCards;
    }

    public void setBankCards(ArrayList<Card> bankCards) {
        BankCards = bankCards;
    }

    public void setDrawCardsAndDiscardPile(DrawPileAndDiscardPile drawCardsAndDiscardPile) {
        this.drawCardsAndDiscardPile = drawCardsAndDiscardPile;
    }

    public void setEnemy(ArrayList<Player> enemy) {
        Enemy = enemy;
    }

    public static int getBestSum() {
        return bestSum;
    }

    public static void setBestSum(int bestSum) {
        Player.bestSum = bestSum;
    }

    public static List<Integer> getBest() {
        return best;
    }

    public static void setBest(List<Integer> best) {
        Player.best = best;
    }



    public Player(DrawPileAndDiscardPile drawCardsAndDiscardPile) {
        Enemy = new ArrayList<>();
        HandCards = new ArrayList<>();
        PropertyCards = new ArrayList<>();
        BankCards = new ArrayList<>();
        this.isOnTurn = false;
        this.drawCardsAndDiscardPile = drawCardsAndDiscardPile;
        this.UseCardTimes = 0;
    }//创建玩家时 创建各种列表 手上的 地产 钱

    public void takeCard(int number) {
        for (int i = 0; i < number; i++) {
            if (drawCardsAndDiscardPile.getDrawPile().isEmpty()) {
                drawCardsAndDiscardPile.getDrawPile().addAll(drawCardsAndDiscardPile.getDiscardPile());
                i-=1;
                continue;
            }
            drawCardsAndDiscardPile.getDrawPile().removeFirst();
            HandCards.add(drawCardsAndDiscardPile.getDrawPile().getFirst());
        }
    }//抓牌 抓几张 参数写几 牌不够了 带洗牌功能

    public void takeMoney(int number,Player player) {
        ArrayList<Card> cards = player.getBankCards();
        int totalMoney = 0;
        int [] eachMoney = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            totalMoney += cards.get(i).getValue();
            eachMoney[i] = cards.get(i).getValue();
        }
        if (totalMoney <= number) {
            BankCards.addAll(player.getBankCards());
            player.getBankCards().clear();
        }else{
            //
        }
    }

    public static List<Integer> find(int[] nums, int target) {
        bestSum = Integer.MAX_VALUE;
        best = new ArrayList<>();

        dfs(nums, target, 0, 0, new ArrayList<>());

        return best;
    }

    static void dfs(int[] nums, int target, int i, int sum, List<Integer> temp) {
        // 超过目标
        if (sum > target) {
            if (sum < bestSum) {
                bestSum = sum;
                best = new ArrayList<>(temp);
            }
            return;
        }

        // 遍历结束
        if (i == nums.length) return;

        // 选当前数
        temp.add(nums[i]);
        dfs(nums, target, i + 1, sum + nums[i], temp);

        // 不选当前数
        temp.remove(temp.size() - 1);
        dfs(nums, target, i + 1, sum, temp);
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
        drawCardsAndDiscardPile.getDiscardPile().add(card);
        switch (card.getActionCardType()) {
            case PASS_GO:
                takeCard(2);
                break;
            case BIRTHDAY:
                for(Player player : Enemy){
                    if(player!=this){takeMoney(2,player);}
                }
                break;
            case DEBT_COLLECTOR:
                if(!Enemy.isEmpty()){
                    takeMoney(5,Enemy.get(0));//要GUI选
                }
        }
    }//对应规则C 行动卡

    public ArrayList<Card> getHandCards() {return HandCards;}

    public ArrayList<PropertiesCards> getPropertyCards() {return PropertyCards;}

    public ArrayList<Card> getBankCards() {return BankCards;}

    public DrawPileAndDiscardPile getDrawCardsAndDiscardPile() {return drawCardsAndDiscardPile;}

    public ArrayList<Player> getEnemy() {return Enemy;}

    /*
    private boolean checkIfWin(ArrayList<Card> cards) {
        for (int i = 0; i < PropertyCards.size(); i++) {
            return true;
        }
        return false;
    }//判断游戏胜利 每个玩家每一个行动 触发检查胜利

     */

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
            // drawCardsAndDiscardPile.getDiscardPile().add(discard);
        }

        isOnTurn = false;
        UseCardTimes = 0;
    }
}
