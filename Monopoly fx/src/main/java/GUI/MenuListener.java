package GUI;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import logic.Game;

public class MenuListener {

    private MainMenu menu;
    private Game game;

    public MenuListener(MainMenu menu, Game game) {
        this.menu = menu;
        this.game = game;
    }

    public void addListener(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.N) {
                System.out.println("按下了 N");
                // TODO: 在这里写你要做的操作
            }

            if (code == KeyCode.A) {
                System.out.println("按下了 A");
                // TODO: 开始新游戏
            }

            if (code == KeyCode.X) {
                System.out.println("按下了 X");
                // TODO: 退出
            }
        });
    }
}
