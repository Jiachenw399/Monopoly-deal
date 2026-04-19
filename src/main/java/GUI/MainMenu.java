package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Animated-free, scene-graph based main menu. Keeps the file name for
 * backwards compatibility with the original Canvas-based menu.
 */
public class MainMenu {

    private final Parent root;

    public MainMenu(MonopolyApp app) {
        Label title = new Label("Monopoly Deal");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 56));
        title.setTextFill(Color.web("#f1c40f"));

        Label subtitle = new Label("A fast card game of property, money and rent.");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        subtitle.setTextFill(Color.web("#ecf0f1"));

        Button newGameBtn = primaryButton("New Game");
        newGameBtn.setOnAction(e -> app.startNewGame());

        Button rulesBtn = primaryButton("Rules");
        rulesBtn.setOnAction(e -> app.showRules());

        Button exitBtn = primaryButton("Exit");
        exitBtn.setOnAction(e -> System.exit(0));

        VBox menu = new VBox(18, title, subtitle, newGameBtn, rulesBtn, exitBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(40));

        StackPane container = new StackPane(menu);
        container.setBackground(new Background(new BackgroundFill(
                Color.web("#0f1e3d"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.root = container;
    }

    public Parent getRoot() {
        return root;
    }

    static Button primaryButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(220);
        b.setPrefHeight(46);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        b.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; "
                + "-fx-background-radius: 8; -fx-cursor: hand;");
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: #ca6f1e; -fx-text-fill: white; "
                        + "-fx-background-radius: 8; -fx-cursor: hand;"));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: #e67e22; -fx-text-fill: white; "
                        + "-fx-background-radius: 8; -fx-cursor: hand;"));
        return b;
    }
}
