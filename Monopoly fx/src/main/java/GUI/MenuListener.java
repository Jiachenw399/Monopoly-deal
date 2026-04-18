package GUI;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import logic.Game;
import model.Card;

import java.util.Scanner;

public class MenuListener {

    private MainMenu menu;
    private Game game;
    private GameScreen  gameScreen;

    public MenuListener(MainMenu menu, Game game, GameScreen gameScreen) {
        this.menu = menu;
        this.game = game;
        this.gameScreen = gameScreen;
    }

    public void addListener(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.N) {
                System.out.println("按下了 N");
                // TODO: 这里需要展示规则图片 或者打印出所有规则
            }

            if (code == KeyCode.A) {
                menu.setShow(false);
                gameScreen.setShow(true);
                game.startGame();
                game.mainLoop();
                // TODO: 开始新游戏
            }

            if (code == KeyCode.X) {
                System.exit(0);
            }
        });
    }
}
