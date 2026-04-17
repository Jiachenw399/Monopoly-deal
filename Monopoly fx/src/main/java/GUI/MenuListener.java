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
                while (!game.isWin()){
                    game.startTurn();
                    //game.getCurrentPlayer().printAllCardsOfHands();
                    Scanner sc  = new Scanner(System.in);
                    System.out.println("Please enter the number of a card to use");
                    int cardNumber = sc.nextInt();
                    Card card = game.getCurrentPlayer().getHandCards().get(cardNumber-1);
                    game.getCurrentPlayer().putCard(card);
                    game.getCurrentPlayer().printAllCardsOfEnemy();
                    game.endTurn();
                }
                // TODO: 开始新游戏
            }

            if (code == KeyCode.X) {
                System.exit(0);
            }
        });
    }
}
