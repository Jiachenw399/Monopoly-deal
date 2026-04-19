package GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import model.ActionCards;
import model.Card;
import model.MoneyCards;
import model.PropertiesCards;

/**
 * Compact visual representation of a single {@link Card}. Styled via inline
 * JavaFX APIs (no CSS file) to keep the project self-contained.
 */
public final class CardView extends StackPane {

    public static final double WIDTH = 90;
    public static final double HEIGHT = 130;

    private final Card card;

    public CardView(Card card, boolean faceDown) {
        this.card = card;
        setPrefSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);

        Color fill;
        String title;
        String subtitle;
        Color textColor = Color.WHITE;

        if (faceDown) {
            fill = Color.web("#1a1a2e");
            title = "MD";
            subtitle = "";
        } else if (card instanceof MoneyCards m) {
            fill = Color.web("#27ae60");
            title = "$" + m.getValue() + "M";
            subtitle = "MONEY";
        } else if (card instanceof PropertiesCards p) {
            fill = Color.web(p.getColor().getHex());
            title = p.getColor().getDisplayName();
            subtitle = "PROPERTY";
            if (isLightColor(fill)) textColor = Color.BLACK;
        } else if (card instanceof ActionCards r) {
            fill = Color.web(r.getColor().getHex()).desaturate();
            title = "RENT";
            subtitle = r.getColor().getDisplayName();
            if (isLightColor(fill)) textColor = Color.BLACK;
        } else {
            fill = Color.GRAY;
            title = "?";
            subtitle = "";
        }

        setBackground(new Background(new BackgroundFill(fill,
                new CornerRadii(8), null)));
        setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2))));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleLabel.setTextFill(textColor);

        Label subLabel = new Label(subtitle);
        subLabel.setFont(Font.font("Segoe UI", 11));
        subLabel.setTextFill(textColor);

        StackPane.setAlignment(titleLabel, Pos.CENTER);
        StackPane.setAlignment(subLabel, Pos.BOTTOM_CENTER);
        subLabel.setStyle("-fx-padding: 0 0 8 0;");

        getChildren().addAll(titleLabel, subLabel);
    }

    public Card getCard() {
        return card;
    }

    private static boolean isLightColor(Paint p) {
        if (!(p instanceof Color c)) return false;
        double luminance = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        return luminance > 0.65;
    }
}
