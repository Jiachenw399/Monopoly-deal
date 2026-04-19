package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Static rules screen. Renders the rules text and a back button.
 */
public class RuleScreen {

    private final Parent root;

    public RuleScreen(MonopolyApp app) {
        Label title = new Label("Game Rules");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#f1c40f"));

        Label rules = new Label(String.join("\n",
                "There are three card categories:",
                "  - Money cards: denominations $1M, $4M, $10M.",
                "  - Property cards: each belongs to one of ten colors.",
                "  - Rent cards: demand rent from ONE opponent in the card's color.",
                "",
                "Starting hand: 5 cards.",
                "",
                "Every turn:",
                "  1. Draw 2 cards (or 5 if your hand is empty).",
                "  2. Play at most ONE card of each category (max three plays).",
                "     - Money goes into your bank.",
                "     - Property goes into your property area.",
                "     - Rent is legal only if YOU own the color AND the target owns it too.",
                "  3. End the turn. If your hand has more than 7 cards, return the",
                "     excess to the bottom of the draw pile.",
                "",
                "Paying rent:",
                "  Pay from your bank using the minimum subset whose total",
                "  is at least the demanded amount. No change is given.",
                "  If your bank total is smaller than the demand, you pay all of it.",
                "",
                "Winning: be the first to complete three full property sets."
        ));
        rules.setFont(Font.font("Consolas", 14));
        rules.setTextFill(Color.web("#ecf0f1"));
        rules.setWrapText(true);

        javafx.scene.control.Button back = MainMenu.primaryButton("Back to Menu");
        back.setOnAction(e -> app.showMainMenu());

        VBox content = new VBox(16, title, rules, back);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30));

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: #0f1e3d; -fx-background-color: #0f1e3d;");

        StackPane container = new StackPane(sp);
        container.setBackground(new Background(new BackgroundFill(
                Color.web("#0f1e3d"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.root = container;
    }

    public Parent getRoot() {
        return root;
    }
}
