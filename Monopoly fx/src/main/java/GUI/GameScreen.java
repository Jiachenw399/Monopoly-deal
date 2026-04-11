package GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.Game;

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

    public void clear(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
