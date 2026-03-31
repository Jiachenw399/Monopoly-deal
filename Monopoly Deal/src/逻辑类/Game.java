package 逻辑类;

import 基础类.Card;
import 基础类.DrawPileAndDiscardPile;
import 基础类.Player;
import 基础类.PropertiesCards;


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

    private boolean reachWinCondition(Player player){
        int winCount = 0;
        int darkBlueCount = 0;
        int brownCount = 0;
        int lightGreenCount = 0;
        int orangeCount = 0;
        int pinkCount = 0;
        int yellowCount = 0;
        int darkGreenCount = 0;
        int lightBlueCount = 0;
        int redCount = 0;
        int blackCount = 0;
        for(PropertiesCards card : player.getPropertyCards()){
            switch (card.getType()){
                case DARK_BLUE:
                    darkBlueCount++;
                    break;
                case BROWN:
                    brownCount++;
                    break;
                case LIGHT_GREEN:
                    lightGreenCount++;
                    break;
                case ORANGE:
                    orangeCount++;
                    break;
                case PINK:
                    pinkCount++;
                    break;
                case YELLOW:
                    yellowCount++;
                    break;
                case DARK_GREEN:
                    darkGreenCount++;
                    break;
                case LIGHT_BLUE:
                    lightBlueCount++;
                    break;
                case RED:
                    redCount++;
                    break;
                case BLACK:
                    blackCount++;
                    break;
            }
            if(darkBlueCount==2){
                winCount++;
            }
            if(brownCount==2){
                winCount++;
            }
            if(lightGreenCount==2){
                winCount++;
            }
            if(orangeCount==3){
                winCount++;
            }
            if(pinkCount==3){
                winCount++;
            }
            if(yellowCount==3){
                winCount++;
            }
            if(darkGreenCount==3){
                winCount++;
            }
            if(lightBlueCount==3){
                winCount++;
            }
            if(redCount==3){
                winCount++;
            }
            if(brownCount==4){
                winCount++;
            }
            if(blackCount==4){
                winCount++;
            }
            if(winCount >= 3){
                return true;
            }
            }
            return false;
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
