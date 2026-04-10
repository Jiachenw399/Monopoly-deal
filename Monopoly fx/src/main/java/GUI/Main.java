package GUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Game;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        MainMenu menu = new MainMenu();
        Game game = new Game();

        Scene scene = new Scene(new javafx.scene.Group(menu.getCanvas()));

        MenuListener menuListener = new MenuListener(menu, game);
        menuListener.addListener(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Monopoly Deal");
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                menu.paint();
            }
        }.start();

        menu.getCanvas().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}