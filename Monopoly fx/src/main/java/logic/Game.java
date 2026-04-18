package logic;

import model.*;

import java.util.ArrayList;
import java.util.Scanner;


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

    //start the game
    public void startGame(){
        currentPlayerIndex = 0;
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.setOnTurn(true);
    }

    public void mainLoop(){
        while(!isWin){
            Player currentPlayer = getCurrentPlayer();
            startTurn(currentPlayer);
            while(currentPlayer.getUseCardTimes()<3){
                int CardIndex = currentPlayer.chooseHandCard()-1;
                if(CardIndex == -1){
                    break;
                }else if(CardIndex<0||CardIndex>currentPlayer.getHandCards().size()-1){
                    System.out.println("Please enter a valid card number");
                }else{
                    currentPlayer.putCard(currentPlayer.getHandCards().get(CardIndex));
                }
            }
            endTurn(currentPlayer);
        }
    }

    //take card
    public void startTurn(Player currentPlayer){
        currentPlayer.setOnTurn(true);
        if(currentPlayer.getHandCards().isEmpty()){
            currentPlayer.takeCard(5);
        }else{
            currentPlayer.takeCard(2);
        }
    }

    public void endTurn(Player currentPlayer){
        if(currentPlayer.checkIfWin()){
            isWin = true;
            //TODO 显示胜利
            return;
        }
        currentPlayer.setOnTurn(false);
        currentPlayer.setUseCardTimes(0);
        currentPlayerIndex = (currentPlayerIndex+1)%players.size();
        startTurn(getCurrentPlayer());
    }

    public void playCard(Card card){
        Player currentPlayer = getCurrentPlayer();
        if(!currentPlayer.isOnTurn()){
            return;
        }

        if(currentPlayer.getUseCardTimes() >= 3){
            return;
        }

        if(card instanceof MoneyCards){
            currentPlayer.putMoneyCard(card);
        }else if(card instanceof PropertiesCards){
            currentPlayer.putPropertyCard((PropertiesCards) card);
        }else if(card instanceof ActionCards){
            currentPlayer.putActionCard((ActionCards) card);
        }

        if(currentPlayer.checkIfWin()){
            isWin = true;
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
