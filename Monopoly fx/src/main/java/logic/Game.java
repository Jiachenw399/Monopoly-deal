package logic;

import model.*;

import java.util.ArrayList;


public class Game {
    private ArrayList<Player> players;
    private DrawPileAndDiscardPile drawCards;
    private boolean isWin;
    public static double SCREEN_WIDTH = 1035;
    public static double SCREEN_HEIGHT = 625;
    private int currentPlayerIndex;

    public Game(){
        players = new ArrayList<>();
        drawCards = new DrawPileAndDiscardPile();
        addPlayer();
        isWin = false;
    }

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
        if (currentPlayer.checkIfWin()) {
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
