package GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Game;

/**
 * JavaFX entry point. Owns the primary stage and swaps between the three
 * top-level views (main menu, rules, game).
 */
public class MonopolyApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Monopoly Deal");
        showMainMenu();
        primaryStage.show();
    }

    public void showMainMenu() {
        setRoot(new MainMenu(this).getRoot());
    }

    public void showRules() {
        setRoot(new RuleScreen(this).getRoot());
    }

    public void startNewGame() {
        Game game = new Game(java.util.List.of("Player 1", "Player 2"));
        game.startGame();
        GameScreen screen = new GameScreen(this, game);
        setRoot(screen.getRoot());
    }

    private void setRoot(Parent root) {
        Scene existing = stage.getScene();
        if (existing == null) {
            stage.setScene(new Scene(root, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));
        } else {
            existing.setRoot(root);
        }
    }
}
