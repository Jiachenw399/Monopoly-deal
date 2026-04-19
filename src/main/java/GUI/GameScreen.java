package GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Game;
import model.Player;

public class GameScreen {
    private Canvas canvas;
    private Game game;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public GameScreen(Game game) {
        this.game = game;
        canvas = new Canvas(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        this.isShow = false;
    }

    public void drawBackground(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0,Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
    }

    private void drawCurrentPlayer() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();
        // 显示当前玩家信息
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("current player: Player " + (game.getCurrentPlayerIndex() + 1), 20, 30);
        gc.fillText("times of playing cards: " + currentPlayer.getUseCardTimes() + "/3", 20, 55);

        if (game.isDiscard()) {
            gc.setFill(Color.RED);
            gc.fillText("you need to discard: " + (currentPlayer.getHandCards().size() - 7) + " cards!", 20, 80);
        }
    }

    private void drawHandCards(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();

        double cardWidth = 80;
        double cardHeight = 120;
        double startX = 50;
        double startY = Game.SCREEN_HEIGHT - 150;
        double gap = 10;
        //111
    }

    public void clear(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
