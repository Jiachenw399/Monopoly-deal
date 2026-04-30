package GUI;

import javafx.scene.Scene;
import model.Card;
import model.Player;

public class GameListener {
    private MainMenu menu;
    private GameScreen gameScreen;
    private logic.Game game;

    public GameListener(MainMenu menu, GameScreen gameScreen, logic.Game game) {
        this.menu = menu;
        this.gameScreen = gameScreen;
        this.game = game;

    }

    public void addListener(Scene scene) {
        scene.setOnMouseClicked(event -> {
            if (!gameScreen.isShow()) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            if (game.isWin()) {
                return;
            }

            if (gameScreen.isEndTurnClicked(x, y)) {
                Player currentPlayer = game.getCurrentPlayer();

                if (!game.isDiscard()) {
                    game.startDiscard();
                }

                if (!game.isDiscard()) {
                    game.endTurn(currentPlayer);
                    game.startTurn(game.getCurrentPlayer());
                }

                return;
            }

            if (gameScreen.isBackMenuClicked(x, y)) {
                gameScreen.setShow(false);
                menu.setShow(true);
                return;
            }

            int handIndex = gameScreen.getClickedHandCardIndex(x, y);

            if (handIndex == -1) {
                return;
            }

            Player currentPlayer = game.getCurrentPlayer();

            if (handIndex >= currentPlayer.getHandCards().size()) {
                return;
            }

            Card selectedCard = currentPlayer.getHandCards().get(handIndex);

            if (game.isDiscard()) {
                game.discard(selectedCard);
                return;
            }

            game.playCard(selectedCard);

            if (currentPlayer.checkIfWin()) {
                game.setWin(true);
            }
        });
    }
}