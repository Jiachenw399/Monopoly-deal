package GUI;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Game;
import model.Card;
import model.Player;
import model.PropertyColor;

public class PlayerViewPanel {
    private static final double BUTTON_X = 760;
    private static final double BUTTON_Y = 90;
    private static final double BUTTON_WIDTH = 100;
    private static final double BUTTON_HEIGHT = 35;
    private static final double BUTTON_GAP = 10;

    private static final double BOX_X = 865;
    private static final double BOX_Y = 85;
    private static final double BOX_WIDTH = 160;
    private static final double BOX_HEIGHT = 430;

    public static void drawPlayerViewButtons(GraphicsContext gc, Game game, int viewedPlayerIndex) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            drawPlayerViewButton(gc, i, i == viewedPlayerIndex);
        }

        gc.setTextBaseline(VPos.TOP);
    }

    public static void drawViewedPlayerInfo(GraphicsContext gc, Game game, int viewedPlayerIndex) {
        if (viewedPlayerIndex < 0 || viewedPlayerIndex >= game.getPlayers().size()) {
            return;
        }

        Player viewedPlayer = game.getPlayers().get(viewedPlayerIndex);

        drawInfoBox(gc);
        drawBasicPlayerInfo(gc, viewedPlayer, viewedPlayerIndex);
        drawMoneyPreview(gc, viewedPlayer);
        drawPropertySetPreview(gc, viewedPlayer);
    }

    public static int getClickedPlayerViewButtonIndex(Game game, double mouseX, double mouseY) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            double buttonY = BUTTON_Y + i * (BUTTON_HEIGHT + BUTTON_GAP);

            if (mouseX >= BUTTON_X && mouseX <= BUTTON_X + BUTTON_WIDTH
                    && mouseY >= buttonY && mouseY <= buttonY + BUTTON_HEIGHT) {
                return i;
            }
        }

        return -1;
    }

    private static void drawPlayerViewButton(GraphicsContext gc, int playerIndex, boolean selected) {
        double y = BUTTON_Y + playerIndex * (BUTTON_HEIGHT + BUTTON_GAP);

        if (selected) {
            gc.setFill(Color.LIGHTGREEN);
        } else {
            gc.setFill(Color.LIGHTGRAY);
        }

        gc.fillRoundRect(BUTTON_X, y, BUTTON_WIDTH, BUTTON_HEIGHT, 10, 10);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(BUTTON_X, y, BUTTON_WIDTH, BUTTON_HEIGHT, 10, 10);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PLAYER " + (playerIndex + 1), BUTTON_X + BUTTON_WIDTH / 2, y + BUTTON_HEIGHT / 2);
    }

    private static void drawInfoBox(GraphicsContext gc) {
        gc.setFill(Color.rgb(240, 240, 240));
        gc.fillRoundRect(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, 15, 15);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(BOX_X, BOX_Y, BOX_WIDTH, BOX_HEIGHT, 15, 15);
    }

    private static void drawBasicPlayerInfo(GraphicsContext gc, Player viewedPlayer, int viewedPlayerIndex) {
        double textX = BOX_X + 10;
        double textY = BOX_Y + 10;

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 15));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);

        gc.fillText("Viewing: P" + (viewedPlayerIndex + 1), textX, textY);
        gc.fillText("Hand: " + viewedPlayer.getHandCards().size(), textX, textY + 25);

        gc.setFill(Color.GREEN);
        gc.fillText("Sets: " + PlayerInfoHelper.getCompletedSetCount(viewedPlayer) + "/3", textX, textY + 50);

        gc.setFill(Color.BLACK);
        gc.fillText("Bank: " + PlayerInfoHelper.getBankTotal(viewedPlayer) + "M", textX, textY + 75);
    }

    private static void drawMoneyPreview(GraphicsContext gc, Player viewedPlayer) {
        double textX = BOX_X + 10;
        double textY = BOX_Y + 10;
        double moneyY = textY + 128;

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 13));
        gc.fillText("Money Cards:", textX, textY + 105);

        if (viewedPlayer.getBankCards().isEmpty()) {
            gc.fillText("None", textX, moneyY);
            return;
        }

        String moneyText = buildMoneyText(viewedPlayer);
        gc.fillText(moneyText, textX, moneyY);

        if (viewedPlayer.getBankCards().size() > 6) {
            gc.fillText("...", textX, moneyY + 18);
        }
    }

    private static String buildMoneyText(Player viewedPlayer) {
        String moneyText = "";
        int moneyCount = 0;

        for (Card card : viewedPlayer.getBankCards()) {
            moneyText += card.getValue() + "M ";
            moneyCount++;

            if (moneyCount >= 6) {
                break;
            }
        }

        return moneyText;
    }

    private static void drawPropertySetPreview(GraphicsContext gc, Player viewedPlayer) {
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.BLACK);
        gc.fillText("Property Sets:", BOX_X + 10, BOX_Y + 190);

        double leftX = BOX_X + 10;
        double rightX = BOX_X + 85;
        double startY = BOX_Y + 220;
        double lineGap = 30;

        PropertyColor[] colors = PropertyColor.values();

        for (int i = 0; i < colors.length; i++) {
            PropertyColor color = colors[i];
            double x = i < 5 ? leftX : rightX;
            double y = i < 5 ? startY + i * lineGap : startY + (i - 5) * lineGap;

            drawPropertySetLine(gc, viewedPlayer, color, x, y);
        }
    }

    private static void drawPropertySetLine(GraphicsContext gc,
                                            Player viewedPlayer,
                                            PropertyColor color,
                                            double x,
                                            double y) {
        int current = PlayerInfoHelper.getPropertyCountByCurrentColor(viewedPlayer, color);
        int need = color.getAmountToCompleteSet();

        if (current >= need) {
            gc.setFill(Color.GREEN);
        } else {
            gc.setFill(Color.BLACK);
        }

        gc.setFont(Font.font("Arial", 10));
        gc.fillText(PlayerInfoHelper.getShortColorName(color) + ": " + current + "/" + need, x, y);

        if (PlayerInfoHelper.hasHotel(viewedPlayer, color)) {
            ScreenIconHelper.drawHotelIcon(gc, x + 45, y - 2);
        } else if (PlayerInfoHelper.hasHouse(viewedPlayer, color)) {
            ScreenIconHelper.drawHouseIcon(gc, x + 45, y - 2);
        }
    }
}