package GUI;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import logic.Game;
import logic.GamePhase;
import model.ActionCards;
import model.Card;
import model.MoneyCards;
import model.PropertiesCards;
import model.Player;
import model.PropertyColor;

/**
 * Main game screen. Uses a scene graph layout (no canvas drawing) so cards
 * and other controls are real, styleable nodes that handle clicks natively.
 *
 * The layout is a {@link BorderPane}:
 *   top:    opponent area (face-down hand, bank total, properties by color)
 *   center: log panel + turn status
 *   bottom: current player area (clickable hand, bank, properties)
 */
public class GameScreen {

    private final MonopolyApp app;
    private final Game game;

    private final BorderPane root = new BorderPane();
    private final VBox topArea = new VBox(10);
    private final VBox bottomArea = new VBox(10);
    private final Label statusLabel = new Label();
    private final Label phaseLabel = new Label();
    private final TextArea logArea = new TextArea();
    private final Button endTurnBtn = new Button("End Turn");

    public GameScreen(MonopolyApp app, Game game) {
        this.app = app;
        this.game = game;

        root.setBackground(new Background(new BackgroundFill(
                Color.web("#0f1e3d"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(12));

        topArea.setAlignment(Pos.TOP_CENTER);
        topArea.setPadding(new Insets(10));
        topArea.setBackground(new Background(new BackgroundFill(
                Color.web("#12264e"), new CornerRadii(10), Insets.EMPTY)));

        bottomArea.setAlignment(Pos.TOP_CENTER);
        bottomArea.setPadding(new Insets(10));
        bottomArea.setBackground(new Background(new BackgroundFill(
                Color.web("#14305f"), new CornerRadii(10), Insets.EMPTY)));

        root.setTop(topArea);
        root.setCenter(buildCenter());
        root.setBottom(bottomArea);

        endTurnBtn.setOnAction(e -> onEndTurnClicked());
        refresh();
    }

    public Parent getRoot() {
        return root;
    }

    // -----------------------------------------------------------------------

    private Parent buildCenter() {
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        statusLabel.setTextFill(Color.web("#f1c40f"));
        phaseLabel.setFont(Font.font("Segoe UI", 14));
        phaseLabel.setTextFill(Color.web("#ecf0f1"));

        logArea.setEditable(false);
        logArea.setPrefRowCount(6);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-control-inner-background: #0a1834; -fx-text-fill: #ecf0f1;"
                + " -fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        Button backBtn = new Button("Quit to Menu");
        backBtn.setOnAction(e -> app.showMainMenu());

        endTurnBtn.setPrefWidth(140);
        endTurnBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;"
                + " -fx-font-weight: bold; -fx-background-radius: 6;");

        HBox controls = new HBox(12, endTurnBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox center = new VBox(8, statusLabel, phaseLabel, logArea, controls);
        center.setPadding(new Insets(12));
        return center;
    }

    /** Rebuilds the entire UI from the current game state. */
    private void refresh() {
        Player me = game.currentPlayer();
        List<Player> opponents = game.opponents();

        topArea.getChildren().setAll();
        for (Player opp : opponents) {
            topArea.getChildren().add(buildPlayerPanel(opp, true));
        }

        bottomArea.getChildren().setAll(buildPlayerPanel(me, false));

        statusLabel.setText("Current turn: " + me.getName()
                + "   |   Winner target: " + Game.SETS_TO_WIN + " sets");
        phaseLabel.setText("Phase: " + game.getPhase()
                + "   |   Money left: " + game.remainingMoneyPlays()
                + "   Property left: " + game.remainingPropertyPlays()
                + "   Rent left: " + game.remainingRentPlays()
                + "   |   Hand: " + me.getHand().size() + "/" + Game.HAND_LIMIT
                + "   Deck: " + game.getDeck().drawPileSize());

        StringBuilder sb = new StringBuilder();
        List<String> log = game.getLog();
        int start = Math.max(0, log.size() - 40);
        for (int i = start; i < log.size(); i++) sb.append(log.get(i)).append('\n');
        logArea.setText(sb.toString());
        logArea.positionCaret(sb.length());

        endTurnBtn.setDisable(game.getPhase() != GamePhase.PLAY);

        switch (game.getPhase()) {
            case GAME_OVER -> {
                showInfo("Game Over", game.getWinner().getName() + " wins the game!");
                app.showMainMenu();
            }
            case AWAITING_PAYMENT -> handlePendingRent();
            case DISCARD -> showInfo("Discard", me.getName()
                    + ", click a card in your hand to return it to the deck ("
                    + (me.getHand().size() - Game.HAND_LIMIT) + " to go).");
            default -> { /* nothing extra to do */ }
        }
    }

    private Parent buildPlayerPanel(Player player, boolean hideHand) {
        Label title = new Label(player.getName()
                + "   (Sets: " + player.completeSetCount()
                + "   Bank: $" + player.bankTotal() + "M)");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#f1c40f"));

        FlowPane properties = new FlowPane(6, 6);
        properties.setAlignment(Pos.CENTER_LEFT);
        for (PropertyColor c : PropertyColor.values()) {
            int cnt = player.propertyCount(c);
            if (cnt == 0) continue;
            Label chip = new Label(c.getDisplayName() + " " + cnt + "/" + c.getSetSize());
            chip.setPadding(new Insets(4, 10, 4, 10));
            chip.setStyle("-fx-background-color: " + c.getHex()
                    + "; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-weight: bold;");
            properties.getChildren().add(chip);
        }

        HBox hand = new HBox(6);
        hand.setAlignment(Pos.CENTER_LEFT);
        if (hideHand) {
            Label countLabel = new Label("Hand: " + player.getHand().size() + " cards");
            countLabel.setTextFill(Color.web("#ecf0f1"));
            for (int i = 0; i < Math.min(player.getHand().size(), 7); i++) {
                hand.getChildren().add(new CardView(null, true));
            }
            hand.getChildren().add(countLabel);
        } else {
            for (Card c : player.getHand()) {
                CardView cv = new CardView(c, false);
                cv.setOnMouseClicked(e -> onHandCardClicked(c));
                hand.getChildren().add(cv);
            }
        }

        ScrollPane handScroll = new ScrollPane(hand);
        handScroll.setFitToHeight(true);
        handScroll.setPrefViewportHeight(CardView.HEIGHT + 10);
        handScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Label bank = new Label("Bank: " + describeBank(player));
        bank.setTextFill(Color.web("#ecf0f1"));

        VBox panel = new VBox(6, title, properties, bank, handScroll);
        panel.setPadding(new Insets(6));
        return panel;
    }

    private String describeBank(Player player) {
        if (player.getBank().isEmpty()) return "(empty)";
        StringBuilder sb = new StringBuilder();
        for (MoneyCards m : player.getBank()) sb.append("$").append(m.getValue()).append("M  ");
        sb.append("= $").append(player.bankTotal()).append("M");
        return sb.toString();
    }

    // --- event handlers -----------------------------------------------------

    private void onHandCardClicked(Card c) {
        if (game.getPhase() == GamePhase.DISCARD) {
            game.discardExcess(c);
            refresh();
            return;
        }
        if (game.getPhase() != GamePhase.PLAY) return;

        try {
            switch (c) {
                case MoneyCards m -> game.playMoney(m);
                case PropertiesCards p -> game.playProperty(p);
                case ActionCards r -> {
                    if (!game.canPlay(r)) {
                        showInfo("Cannot play",
                                "Rent requires you and a target opponent to own "
                                        + r.getColor().getDisplayName() + ".");
                        return;
                    }
                    Player target = pickRentTarget(r);
                    if (target == null) return;
                    game.playRent(r, target);
                }
                default -> { /* unknown card type, ignore */ }
            }
        } catch (IllegalStateException | IllegalArgumentException ex) {
            showInfo("Cannot play", ex.getMessage());
        }
        refresh();
    }

    private Player pickRentTarget(ActionCards rent) {
        List<Player> candidates = game.validRentTargets(rent.getColor());
        if (candidates.isEmpty()) return null;
        if (candidates.size() == 1) return candidates.get(0);
        ChoiceDialog<Player> dialog = new ChoiceDialog<>(candidates.get(0), candidates);
        dialog.setTitle("Rent target");
        dialog.setHeaderText("Collect rent from which player?");
        dialog.setContentText("Target:");
        Optional<Player> res = dialog.showAndWait();
        return res.orElse(null);
    }

    private void handlePendingRent() {
        int amount = game.getPendingRent().getAmount();
        Player payer = game.getPendingRent().getPayer();
        List<MoneyCards> suggested = payer.computeMinSufficientPayment(amount);
        int paid = suggested.stream().mapToInt(MoneyCards::getValue).sum();
        String detail = suggested.isEmpty()
                ? "The target has no money; nothing is transferred."
                : "Payment: " + suggested.size() + " card(s), total $" + paid + "M";
        showInfo("Rent settlement",
                payer.getName() + " must pay $" + amount + "M on "
                        + game.getPendingRent().getColor().getDisplayName() + ".\n" + detail);
        game.resolveRent();
        refresh();
    }

    private void onEndTurnClicked() {
        try {
            game.endTurn();
        } catch (IllegalStateException ex) {
            showInfo("Cannot end turn", ex.getMessage());
        }
        refresh();
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
