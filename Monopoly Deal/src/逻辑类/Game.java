package 逻辑类;

import 基础类.DrawCards;
import 基础类.Player;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private DrawCards drawCards;
    private boolean isWin;

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
