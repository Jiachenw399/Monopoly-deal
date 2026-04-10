package GUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        MainMenu menu = new MainMenu();

        Scene scene = new Scene(new javafx.scene.Group(menu.getCanvas()));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Monopoly Deal");
        primaryStage.show();

        // 👇 游戏循环
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                menu.paint(); // 一直画
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}