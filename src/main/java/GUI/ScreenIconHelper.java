package GUI;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScreenIconHelper {
    public static void drawHotelIcon(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(x + 2, y + 2, 14, 14);

        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(x + 5, y + 5, 3, 3);
        gc.fillRect(x + 10, y + 5, 3, 3);
        gc.fillRect(x + 5, y + 10, 3, 3);
        gc.fillRect(x + 10, y + 10, 3, 3);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(x + 2, y + 2, 14, 14);
    }

    public static void drawHouseIcon(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.DARKRED);
        gc.fillPolygon(
                new double[]{x, x + 8, x + 16},
                new double[]{y + 8, y, y + 8},
                3
        );

        gc.setFill(Color.RED);
        gc.fillRect(x + 2, y + 8, 12, 8);

        gc.setStroke(Color.BLACK);
        gc.strokePolygon(
                new double[]{x, x + 8, x + 16},
                new double[]{y + 8, y, y + 8},
                3
        );
        gc.strokeRect(x + 2, y + 8, 12, 8);
    }
}
