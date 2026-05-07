package GUI;

import javafx.scene.Scene;
import model.Card;
import model.Player;
import model.PropertiesCards;
import model.PropertyColor;
import model.ActionCards;
import model.ActionCardType;

public class GameListener {
    private MainMenu menu;
    private GameScreen gameScreen;
    private logic.Game game;

    public GameListener(MainMenu menu, GameScreen gameScreen, logic.Game game) {
        this.menu = menu;
        this.gameScreen = gameScreen;
        this.game = game;
    }

    public void addListener(Scene scene) {
        scene.setOnMouseClicked(event -> {
            if (!gameScreen.isShow()) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            if (gameScreen.isSlyDealSelecting()) {
                if (gameScreen.isSlyDealCancelClicked(x, y)) {
                    gameScreen.cancelSlyDealSelection();
                    return;
                }

                GameScreen.SlyDealChoice choice = gameScreen.getClickedSlyDealChoice(x, y);

                if (choice != null) {
                    game.finishSlyDeal(
                            gameScreen.getPendingSlyDealCard(),
                            choice.getTargetPlayer(),
                            choice.getSelectedCard()
                    );

                    gameScreen.cancelSlyDealSelection();
                }

                return;
            }

            if (gameScreen.isDebtCollectorSelecting()) {
                if (gameScreen.isDebtCollectorCancelClicked(x, y)) {
                    gameScreen.cancelDebtCollectorSelection();
                    return;
                }

                Player targetPlayer = gameScreen.getClickedDebtCollectorTarget(x, y);

                if (targetPlayer != null) {
                    game.finishDebtCollector(
                            gameScreen.getPendingDebtCollectorCard(),
                            targetPlayer
                    );

                    gameScreen.cancelDebtCollectorSelection();
                }

                return;
            }

            if (gameScreen.isTwoColorRentSelecting()) {
                if (gameScreen.isTwoColorRentCancelClicked(x, y)) {
                    gameScreen.cancelTwoColorRentSelection();
                    return;
                }

                PropertyColor selectedRentColor = gameScreen.getClickedTwoColorRentColor(x, y);

                if (selectedRentColor != null) {
                    game.finishTwoColorRent(
                            gameScreen.getPendingTwoColorRentCard(),
                            selectedRentColor
                    );

                    gameScreen.cancelTwoColorRentSelection();
                }

                return;
            }

            if (game.isWin()) {
                return;
            }

            PropertyColor selectedColor = gameScreen.getClickedWildColorButton(x, y);

            if (selectedColor != null) {
                PropertiesCards selectedWildCard = gameScreen.getSelectedWildCard();

                if (selectedWildCard != null) {
                    selectedWildCard.setCurrentColor(selectedColor);
                }

                gameScreen.setSelectedWildCard(null);
                return;
            }

            PropertiesCards clickedWildCard = gameScreen.getClickedWildCard(x, y);

            if (clickedWildCard != null) {
                gameScreen.setSelectedWildCard(clickedWildCard);
                return;
            }

            if (gameScreen.isEndTurnClicked(x, y)) {
                game.guiEndTurn();
                return;
            }

            if (gameScreen.isBackMenuClicked(x, y)) {
                gameScreen.setShow(false);
                menu.setShow(true);
                return;
            }

            int viewedPlayerIndex = gameScreen.getClickedPlayerViewButtonIndex(x, y);

            if (viewedPlayerIndex != -1) {
                gameScreen.setViewedPlayerIndex(viewedPlayerIndex);
                return;
            }

            int handIndex = gameScreen.getClickedHandCardIndex(x, y);

            if (handIndex == -1) {
                return;
            }

            Player currentPlayer = game.getCurrentPlayer();

            if (handIndex >= currentPlayer.getHandCards().size()) {
                return;
            }

            Card selectedCard = currentPlayer.getHandCards().get(handIndex);

            if (game.isDiscard()) {
                game.discard(selectedCard);
                return;
            }

            if (selectedCard instanceof ActionCards actionCard) {
                if (actionCard.getActionCardType() == ActionCardType.SLY_DEAL) {
                    gameScreen.startSlyDealSelection(actionCard);
                    return;
                }

                if (actionCard.getActionCardType() == ActionCardType.DEBT_COLLECTOR) {
                    gameScreen.startDebtCollectorSelection(actionCard);
                    return;
                }

                if (isTwoColorRentCard(actionCard)) {
                    gameScreen.startTwoColorRentSelection(actionCard);
                    return;
                }
            }

            game.playCard(selectedCard);

            if (currentPlayer.checkIfWin()) {
                game.setWin(true);
            }
        });
    }

    private boolean isTwoColorRentCard(ActionCards card) {
        ActionCardType type = card.getActionCardType();

        return type == ActionCardType.RENT_WITH_RED_AND_YELLOW
                || type == ActionCardType.RENT_WITH_ORANGE_AND_PINK
                || type == ActionCardType.RENT_WITH_BROWN_AND_LIGHT_BLUE
                || type == ActionCardType.RENT_WITH_BLACK_AND_LIGHT_GREEN
                || type == ActionCardType.RENT_WITH_DARK_BLUE_AND_DARK_GREEN;
    }
}