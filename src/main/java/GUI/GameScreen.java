package GUI;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Game;
import java.util.ArrayList;
import model.*;


public class GameScreen {
    private Canvas canvas;
    private Game game;
    private boolean isShow;

    private double cardWidth = 90;
    private double cardHeight = 125;
    private double gap = 12;
    private double handStartX = 35;
    private double handStartY = Game.SCREEN_HEIGHT - 150;

    public GameScreen(Game game) {
        this.game = game;
        canvas = new Canvas(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        this.isShow = false;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void paint() {
        drawBackground();
        drawCurrentPlayer();
        drawDeckInfo();
        drawBankCards();
        drawPropertyCards();
        drawHandCards();
        drawButtons();
        drawWinMessage();
    }

    public void drawBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        gc.setFill(Color.rgb(25, 34, 55));
        gc.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
    }

    private void drawCurrentPlayer() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);

        gc.fillText("Current Player: Player " + (game.getCurrentPlayerIndex() + 1), 20, 20);
        gc.fillText("Played Cards: " + currentPlayer.getUseCardTimes() + "/3", 20, 45);

        if (game.isDiscard()) {
            gc.setFill(Color.RED);
            gc.fillText("Discard Phase: You must discard " + (currentPlayer.getHandCards().size() - 7) + " card(s).", 20, 70);
        } else {
            gc.setFill(Color.LIGHTYELLOW);
            gc.fillText("Click a hand card to play it. Press END TURN button to finish.", 20, 70);
        }
    }

    private void drawDeckInfo() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Draw Pile: " + game.getDrawCards().getDrawPile().size(), 780, 20);
        gc.fillText("Discard Pile: " + game.getDrawCards().getDiscardPile().size(), 780, 45);
    }

    private void drawBankCards() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Bank Area", 20, 115);

        double x = 20;
        double y = 145;

        int total = 0;
        for (Card card : currentPlayer.getBankCards()) {
            total += card.getValue();
        }

        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Total Money: " + total + "M", 120, 115);

        int index = 0;
        for (Card card : currentPlayer.getBankCards()) {
            drawSmallCard(gc, x + index * 65, y, "Money", card.getValue() + "M", Color.GOLD);
            index++;
        }
    }

    private void drawPropertyCards() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Property Area", 20, 255);

        double x = 20;
        double y = 285;

        int index = 0;
        for (PropertiesCards card : currentPlayer.getPropertyCards()) {
            String colorText = card.getCurrentColor() == null ? "NO COLOR" : card.getCurrentColor().name();
            drawSmallCard(gc, x + index * 75, y, "Property", colorText, Color.LIGHTBLUE);
            index++;
        }
    }

    private void drawHandCards() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Card> handCards = currentPlayer.getHandCards();

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Hand Cards", 20, Game.SCREEN_HEIGHT - 180);

        for (int i = 0; i < handCards.size(); i++) {
            double x = handStartX + i * (cardWidth + gap);
            double y = handStartY;

            Card card = handCards.get(i);
            drawHandCard(gc, card, x, y, i + 1);
        }
    }

    private void drawHandCard(GraphicsContext gc, Card card, double x, double y, int number) {
        Color color = Color.WHITE;
        String type = "";
        String name = "";
        String value = card.getValue() + "M";

        if (card instanceof MoneyCards) {
            color = Color.GOLD;
            type = "Money";
            name = value;
        } else if (card instanceof PropertiesCards propertyCard) {
            color = Color.LIGHTBLUE;
            type = "Property";
            name = propertyCard.getType().name();
        } else if (card instanceof ActionCards actionCard) {
            color = Color.LIGHTPINK;
            type = "Action";
            name = actionCard.getActionCardType().name();
        }

        gc.setFill(color);
        gc.fillRoundRect(x, y, cardWidth, cardHeight, 15, 15);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, cardWidth, cardHeight, 15, 15);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 13));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(number + "", x + cardWidth / 2, y + 8);
        gc.fillText(type, x + cardWidth / 2, y + 30);
        gc.fillText(value, x + cardWidth / 2, y + 50);

        gc.setFont(Font.font("Arial", 10));
        drawWrappedText(gc, name, x + 6, y + 70, cardWidth - 12, 12);
    }

    private void drawSmallCard(GraphicsContext gc, double x, double y, String type, String text, Color color) {
        gc.setFill(color);
        gc.fillRoundRect(x, y, 60, 85, 12, 12);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, 60, 85, 12, 12);

        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 10));
        gc.fillText(type, x + 30, y + 15);
        drawWrappedText(gc, text, x + 5, y + 35, 50, 11);
    }

    private void drawButtons() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawButton(gc, 820, 520, 170, 40, "END TURN");
        drawButton(gc, 820, 570, 170, 40, "BACK MENU");
    }

    private void drawButton(GraphicsContext gc, double x, double y, double w, double h, String text) {
        gc.setFill(Color.ORANGE);
        gc.fillRoundRect(x, y, w, h, 12, 12);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, w, h, 12, 12);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x + w / 2, y + h / 2);
        gc.setTextBaseline(VPos.TOP);
    }

    private void drawWinMessage() {
        if (!game.isWin()) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", 42));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Player " + (game.getCurrentPlayerIndex() + 1) + " Wins!", Game.SCREEN_WIDTH / 2, Game.SCREEN_HEIGHT / 2);
    }

    private void drawWrappedText(GraphicsContext gc, String text, double x, double y, double maxWidth, double lineHeight) {
        String[] parts = text.split("_");
        String line = "";
        double currentY = y;

        for (String part : parts) {
            String testLine = line.isEmpty() ? part : line + "_" + part;

            if (testLine.length() > 12) {
                gc.fillText(line, x + maxWidth / 2, currentY);
                line = part;
                currentY += lineHeight;
            } else {
                line = testLine;
            }
        }

        if (!line.isEmpty()) {
            gc.fillText(line, x + maxWidth / 2, currentY);
        }
    }

    public int getClickedHandCardIndex(double mouseX, double mouseY) {
        Player currentPlayer = game.getCurrentPlayer();

        for (int i = 0; i < currentPlayer.getHandCards().size(); i++) {
            double x = handStartX + i * (cardWidth + gap);
            double y = handStartY;

            if (mouseX >= x && mouseX <= x + cardWidth && mouseY >= y && mouseY <= y + cardHeight) {
                return i;
            }
        }

        return -1;
    }

    public boolean isEndTurnClicked(double mouseX, double mouseY) {
        return mouseX >= 820 && mouseX <= 990 && mouseY >= 520 && mouseY <= 560;
    }

    public boolean isBackMenuClicked(double mouseX, double mouseY) {
        return mouseX >= 820 && mouseX <= 990 && mouseY >= 570 && mouseY <= 610;
    }

    public void clear() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}