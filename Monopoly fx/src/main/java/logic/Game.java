package logic;

import model.*;

import java.util.ArrayList;


public class Game {
    private ArrayList<Player> players;
    private DrawPileAndDiscardPile drawCards;
    private boolean isWin;
    public static double SCREEN_WIDTH = 690;
    public static double SCREEN_HEIGHT = 450;
    private int currentPlayerIndex;

    public Game(){
        players = new ArrayList<>();
        drawCards = new DrawPileAndDiscardPile();
        addPlayer();
        isWin = false;
    }
    //initiate player's hand cards
    public void startGame(){
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setOnTurn(true);
        if(currentPlayer.getHandCards().isEmpty()){
            currentPlayer.takeCard(5);
        }else{
            currentPlayer.takeCard(2);
        }
    }
    //turn to the next player
    public void nextPlayer(){
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setOnTurn(false);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setOnTurn(true);
        if(currentPlayer.getHandCards().isEmpty()){
            currentPlayer.takeCard(5);
        }else{
            currentPlayer.takeCard(2);
        }
        currentPlayer.OnTurn();
        if (checkIfWin()) {
            isWin = true;
            // TODO: 显示胜利界面
        }
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    private boolean checkIfWin() {
        for (Player player : players) {
            if(reachWinCondition(player)){
                return true;
            }
        }
        return false;
    }//判断游戏胜利 每个玩家每一个行动 触发检查胜利

    /**
     * 检查指定玩家是否达到获胜条件
     * 根据Monopoly Deal规则：第一个收集到 3 套完整不同颜色地产组的玩家获胜
     * 已考虑万能牌（Wild Cards）可动态分配到任意颜色补足套组
     *
     * @param player 需要检查的玩家
     * @return true 如果该玩家已获胜，否则 false
     */
    private boolean reachWinCondition(Player player) {
        ArrayList<PropertiesCards> properties = player.getPropertyCards();

        if (properties == null || properties.isEmpty()) {
            return false;
        }

        // 1. 按基础颜色统计固定颜色卡牌数量
        java.util.Map<PropertiesCardsType, Integer> colorCount = new java.util.HashMap<>();
        int wildCardCount = 0;  // 万能牌总数（可分配到任意颜色）

        for (PropertiesCards card : properties) {
            PropertiesCardsType type = card.getType();
            if (isWildCard(type)) {  // 判断是否为万能牌
                wildCardCount++;
            } else {
                // 只统计基础颜色（非万能）
                colorCount.put(type, colorCount.getOrDefault(type, 0) + 1);
            }
        }

        // 2. 计算已完成的完整套组数量（使用万能牌补足）
        int completedSets = 0;

        // 遍历所有可能的颜色，检查是否能形成完整套组
        for (PropertiesCardsType color : PropertiesCardsType.values()) {
            if (isWildCard(color)) {
                continue;  // 跳过万能牌类型本身
            }

            int required = color.getColorValue();  // 从枚举获取所需张数（例如深蓝2、黑色4等）
            int current = colorCount.getOrDefault(color, 0);

            // 使用万能牌补足差额（每张万能牌只能用于一个套组）
            int needed = required - current;
            if (needed > 0 && wildCardCount >= needed) {
                current += needed;
                wildCardCount -= needed;  // 万能牌已被消耗，不能重复使用
            }

            // 如果达到或超过所需张数，则视为完成一套完整套组
            if (current >= required) {
                completedSets++;
            }
        }

        // 获胜条件：至少3套完整且不同颜色的套组
        return completedSets >= 3;
    }

    /**
     * 辅助方法：判断是否为万能牌类型
     */
    private boolean isWildCard(PropertiesCardsType type) {
        return type == PropertiesCardsType.WILD_CARDS_WITH_MULTIPLE_COLOR ||
                type.name().startsWith("WILD_CARDS_WITH_");
    }


    private void addPlayer() {//有bug
        for (int i = 0; i < 4; i++) {
            Player p = new Player(drawCards);
            players.add(p);
        }//加入四个玩家

        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size(); j++) {
                if(i!=j){
                    players.get(i).getEnemy().add(players.get(j));
                }
            }
        }//加入后 给每个玩家加上另外的三个敌人

        for (int i = 0; i < players.size(); i++) {
            players.get(i).takeCard(5);
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public DrawPileAndDiscardPile getDrawCards() {return drawCards;}

    public void setDrawCards(DrawPileAndDiscardPile drawCards) {this.drawCards = drawCards;}

    public static double getScreenWidth() {return SCREEN_WIDTH;}

    public static void setScreenWidth(double screenWidth) {SCREEN_WIDTH = screenWidth;}

    public static double getScreenHeight() {return SCREEN_HEIGHT;}

    public static void setScreenHeight(double screenHeight) {SCREEN_HEIGHT = screenHeight;}


}
