package GUI;

import javafx.application.Application;
import javafx.stage.Stage;

import model.MoneyCards;

import logic.Game;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 这里是你 JavaFX 窗口初始化的地方
        // 在这里，你可以创建界面元素并展示它
        Game g = new Game();
        MoneyCards c = new MoneyCards(5);
        g.getPlayers().getFirst().getBankCards().add(c);
        g.getPlayers().get(1).takeMoney(3, g.getPlayers().get(0));
    }

    public static void main(String[] args) {
        launch(args); // JavaFX 启动
    }
}