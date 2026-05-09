package GUI;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ScreenDrawHelper {
    public static void drawButton(GraphicsContext gc,
                                  double x,
                                  double y,
                                  double width,
                                  double height,
                                  String text) {
        gc.setFill(Color.ORANGE);
        gc.fillRoundRect(x, y, width, height, 12, 12);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, width, height, 12, 12);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x + width / 2, y + height / 2);
        gc.setTextBaseline(VPos.TOP);
    }

    public static void drawDisabledButton(GraphicsContext gc,
                                          double x,
                                          double y,
                                          double width,
                                          double height,
                                          String text) {
        gc.setFill(Color.GRAY);
        gc.fillRoundRect(x, y, width, height, 12, 12);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, width, height, 12, 12);

        gc.setFill(Color.DARKGRAY);
        gc.setFont(Font.font("Arial", 15));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x + width / 2, y + height / 2);
        gc.setTextBaseline(VPos.TOP);
    }

    public static void drawSmallCard(GraphicsContext gc,
                                     double x,
                                     double y,
                                     String type,
                                     String text,
                                     Color color) {
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

    public static void drawWrappedText(GraphicsContext gc,
                                       String text,
                                       double x,
                                       double y,
                                       double maxWidth,
                                       double lineHeight) {
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
}