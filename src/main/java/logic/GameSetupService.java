package logic;

import model.DrawPileAndDiscardPile;
import model.Player;

import java.util.ArrayList;

public class GameSetupService {
    private static final int DEFAULT_PLAYER_COUNT = 4;
    private static final int INITIAL_CARD_COUNT = 5;

    public void setupPlayers(ArrayList<Player> players, DrawPileAndDiscardPile drawCards) {
        setupPlayers(players, drawCards, DEFAULT_PLAYER_COUNT);
    }

    public void setupPlayers(ArrayList<Player> players, DrawPileAndDiscardPile drawCards, int playerCount) {
        players.clear();
        createPlayers(players, drawCards, playerCount);
        addEnemiesForPlayers(players);
        dealInitialCards(players);
    }

    private void createPlayers(ArrayList<Player> players, DrawPileAndDiscardPile drawCards, int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            players.add(new Player(drawCards));
        }
    }

    private void addEnemiesForPlayers(ArrayList<Player> players) {
        for (Player player : players) {
            player.getEnemy().clear();

            for (Player otherPlayer : players) {
                if (player != otherPlayer) {
                    player.getEnemy().add(otherPlayer);
                }
            }
        }
    }

    private void dealInitialCards(ArrayList<Player> players) {
        for (Player player : players) {
            player.takeCard(INITIAL_CARD_COUNT);
        }
    }
}
