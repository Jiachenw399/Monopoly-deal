package 逻辑类;

import 基础类.DrawCards;
import 基础类.Player;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private DrawCards drawCards;
    private boolean isWin;
    public static double SCREEN_WIDTH = 690;
    public static double SCREEN_HEIGHT = 450;

    public DrawCards getDrawCards() {
        return drawCards;
    }

    public void setDrawCards(DrawCards drawCards) {
        this.drawCards = drawCards;
    }

    public static double getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static void setScreenWidth(double screenWidth) {
        SCREEN_WIDTH = screenWidth;
    }

    public static double getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static void setScreenHeight(double screenHeight) {
        SCREEN_HEIGHT = screenHeight;
    }

    public Game(){
        players = new ArrayList<>();
        addPlayer();
        drawCards = new DrawCards();
        isWin = false;
    }

    public void playerPlay(Player p){

    }

    private void addPlayer() {
        for (int i = 0; i < 4; i++) {
            Player p = new Player(drawCards);
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
}
