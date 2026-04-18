package model;

import java.util.*;


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
            HandCards.add(drawCardsAndDiscardPile.getDrawPile().remove(0));
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
            //这玩意有bug
        }
    }

    public void putCard(Card card) {
        switch (card) {
            case MoneyCards moneyCards -> putMoneyCard(moneyCards);
            case PropertiesCards propertiesCards -> putPropertyCard(propertiesCards);
            case ActionCards actionCards -> putActionCard(actionCards);
            case null, default -> {
                return;
            }
        }
        UseCardTimes++;
    }//合并一下出牌功能

    public void putMoneyCard(Card card) {
        if(card.getClass().equals(PropertiesCards.class)){
            return;
        }
        HandCards.remove(card);
        BankCards.add(card);
    }//用钱 对应规则A

    public PropertyColor choosePropertyColorForWildCards(PropertiesCards card) {
        ArrayList<PropertyColor> colorsCanBeUsed = card.getType().getColors();
        int i = 1;
        for (PropertyColor color : colorsCanBeUsed) {
            System.out.println(i+color.toString());
            i+=1;
        }
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        System.out.println("What is your property color?");
        if(choice<=0 || choice >=colorsCanBeUsed.size()){
            System.out.println("Invalid choice.");
            return choosePropertyColorForWildCards(card);
        }else{
            card.setCurrentColor(colorsCanBeUsed.get(choice-1));
            return colorsCanBeUsed.get(choice-1);
        }
    }

    public void putPropertyCard(PropertiesCards card) {
        HandCards.remove(card);
        if(card.getType().name().startsWith("WILD")){
            card.setCurrentColor(choosePropertyColorForWildCards(card));
        }
        PropertyCards.add(card);
    }//放地产 对应规则B

    public void putActionCard(ActionCards card) {
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
            case SLY_DEAL:
                if(!Enemy.isEmpty()){
                    Player target = Enemy.get(2);//GUI来选
//                    ArrayList<PropertiesCards> stealTarget = getStealTarget(target);
//                    if(!stealTarget.isEmpty()){
//                        PropertiesCards propToSteal = stealTarget.get(0);//GUI选要steal的
//                        steal(target, propToSteal);
//                    }

                }
        }
    }//对应规则C 行动卡

//    public ArrayList<PropertiesCards> getStealTarget(Player p){
//        ArrayList<PropertiesCards> canBeSteal = new ArrayList<>();
//        ArrayList<PropertiesCards> allProperties = p.getPropertyCards();
//        java.util.Map<PropertiesCardsType, Integer> colorCount = new java.util.HashMap<>();
//        for (PropertiesCards prop : allProperties) {
//            PropertiesCardsType type = prop.getType();
//            if (!isWildCard(type)) {
//                colorCount.put(type, colorCount.getOrDefault(type, 0) + 1);
//            }
//        }
//        for(PropertiesCards prop : allProperties){
//            PropertiesCardsType type = prop.getType();
//            //这个地方逻辑不太对，没想好咋改
//            if(isWildCard(type)){
//                canBeSteal.add(prop);
//                continue;
//            }
//            int totalNum = type.getColorValue();
//            int currentNum = colorCount.get(type);
//            //如果目前地产卡没集齐，可以偷
//            if(currentNum != totalNum){
//                canBeSteal.add(prop);
//            }
//
//        }
//        return canBeSteal;
//    }

    private void steal(Player p, PropertiesCards prop){
        p.getPropertyCards().remove(prop);
        this.PropertyCards.add(prop);
    }//用于偷一个牌

    private void steal(Player p, ArrayList<PropertiesCards> props){
        p.getPropertyCards().removeAll(props);
        this.PropertyCards.addAll(props);
    }//用于偷一组牌

    public ArrayList<Card> getHandCards() {return HandCards;}

    public ArrayList<PropertiesCards> getPropertyCards() {return PropertyCards;}

    public ArrayList<Card> getBankCards() {return BankCards;}

    public DrawPileAndDiscardPile getDrawCardsAndDiscardPile() {return drawCardsAndDiscardPile;}

    public ArrayList<Player> getEnemy() {return Enemy;}


    public boolean checkIfWin() {
        if (PropertyCards == null || PropertyCards.isEmpty()) {
            return false;
        }
        Map <PropertyColor, Integer> colorCount = new java.util.HashMap<>();
        for (int i = 0; i < PropertyCards.size(); i++) {
            colorCount.put(PropertyCards.get(i).getCurrentColor(), colorCount.getOrDefault(PropertyCards.get(i).getCurrentColor(), 0) + 1);
        }
        int completedSets = 0;
        for (PropertyColor color : colorCount.keySet()) {
            int count = colorCount.get(color);
            // 关键：判断是否达到该颜色需要的数量
            if (count >= color.getAmountToCompleteSet()) {
                completedSets++;
            }
        }
        // Monopoly Deal规则：3套获胜
        return completedSets >= 3;
    }//这个方法 大概没问题了


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

    public void printAllCardsOfHands(){
        int i = 1;
        System.out.println("To end a turn, please enter 0");
        for (Card handCard : HandCards) {
            if (handCard instanceof MoneyCards) {
                System.out.println(i+". Money Card: " + handCard.getValue()+ "M$ ");
            }else if(handCard instanceof PropertiesCards){
                System.out.println(i+". Properties Card: " + handCard.getValue()+ "M$ "+((PropertiesCards) handCard).getType());
            }else if(handCard instanceof ActionCards){
                System.out.println(i+". Action Card: "+ handCard.getValue()+ "M$ "+((ActionCards) handCard).getActionCardType());
            }
            i+=1;
        }
    }

    public void printAllCardsOfEnemy(){
        for (Player player : Enemy) {
            for (int i1 = 0; i1 < player.getBankCards().size(); i1++) {
                System.out.println("Money Card: " + player.getBankCards().get(i1).getValue()+ "M$");
            }
            for (int i1 = 0; i1 < player.getPropertyCards().size(); i1++) {
                PropertiesCards propertyCards = (PropertiesCards) player.getPropertyCards().get(i1);
                System.out.println("Properties Card: " + propertyCards.getValue()+ "M$"+propertyCards.getType());
            }
        }
    }

    public int chooseHandCard(){
        Scanner sc  = new Scanner(System.in);
        printAllCardsOfHands();
        System.out.println("Please enter the number of a card to use");
        return sc.nextInt();
    }
}
