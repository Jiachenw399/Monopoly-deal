package GUI;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import logic.Game;

public class MenuListener {

    private MainMenu menu;
    private Game game;
    private GameScreen gameScreen;
    private RuleScreen ruleScreen;

    public MenuListener(MainMenu menu, Game game, GameScreen gameScreen, RuleScreen ruleScreen) {
        this.menu = menu;
        this.game = game;
        this.gameScreen = gameScreen;
        this.ruleScreen = ruleScreen;
    }

    public void addListener(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.N) {
                menu.setShow(false);
                ruleScreen.setShow(true);
            }

            if (code == KeyCode.A) {
                menu.setShow(false);
                ruleScreen.setShow(false);
                gameScreen.setShow(true);

                game.startGame();
                game.startTurn(game.getCurrentPlayer());
            }

            if (code == KeyCode.X) {
                System.exit(0);
            }

            if (code == KeyCode.ESCAPE) {
                ruleScreen.setShow(false);
                gameScreen.setShow(false);
                menu.setShow(true);
            }
        });
    }
}