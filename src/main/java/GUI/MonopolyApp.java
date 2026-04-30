package GUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Game;

public class MonopolyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenu menu = new MainMenu();
        Game game = new Game();
        GameScreen gameScreen = new GameScreen(game);
        RuleScreen ruleScreen = new RuleScreen();

        Group root = new Group();
        root.getChildren().addAll(
                menu.getCanvas(),
                gameScreen.getCanvas(),
                ruleScreen.getCanvas()
        );

        Scene scene = new Scene(root);

        MenuListener menuListener = new MenuListener(menu, game, gameScreen, ruleScreen);
        menuListener.addListener(scene);

        GameListener gameListener = new GameListener(menu, gameScreen, game);
        gameListener.addListener(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Monopoly Deal");
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (menu.isShow()) {
                    menu.paint();
                    menu.getCanvas().setVisible(true);
                } else {
                    menu.clear();
                    menu.getCanvas().setVisible(false);
                }

                if (gameScreen.isShow()) {
                    gameScreen.paint();
                    gameScreen.getCanvas().setVisible(true);
                } else {
                    gameScreen.clear();
                    gameScreen.getCanvas().setVisible(false);
                }

                if (ruleScreen.isShow()) {
                    ruleScreen.paint();
                    ruleScreen.getCanvas().setVisible(true);
                } else {
                    ruleScreen.clear();
                    ruleScreen.getCanvas().setVisible(false);
                }
            }
        }.start();

        root.requestFocus();
    }
}