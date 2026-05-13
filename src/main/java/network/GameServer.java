package network;

import logic.Game;
import model.ActionCards;
import model.Card;
import model.MoneyCards;
import model.Player;
import model.PropertiesCards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {
    private static final int PORT = 5555;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final AtomicInteger nextPlayerId = new AtomicInteger(1);
    private final List<ClientHandler> clients = new ArrayList<>();
    private boolean gameStarted = false;
    private Game game;

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        System.out.println("Server starting on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is ready. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (isServerFull()) {
                    rejectClient(clientSocket);
                    continue;
                }

                ClientHandler clientHandler = new ClientHandler(clientSocket, nextPlayerId.getAndIncrement());
                addClient(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private synchronized boolean isServerFull() {
        return clients.size() >= MAX_PLAYERS;
    }

    private void rejectClient(Socket clientSocket) {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                Socket socketToClose = clientSocket
        ) {
            out.println(new NetworkMessage("FULL", "Server already has " + MAX_PLAYERS + " players").encode());
        } catch (IOException e) {
            System.out.println("Failed to reject client: " + e.getMessage());
        }
    }

    private synchronized void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        System.out.println("Player " + clientHandler.getPlayerId() + " connected. Players: " + clients.size());
    }

    private synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Player " + clientHandler.getPlayerId() + " disconnected. Players: " + clients.size());
    }

    private synchronized int getPlayerCount() {
        return clients.size();
    }

    private synchronized String getPlayerListText() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < clients.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append("PLAYER ").append(clients.get(i).getPlayerId());
        }

        return builder.toString();
    }

    private synchronized void broadcast(NetworkMessage message) {
        String encodedMessage = message.encode();

        for (ClientHandler client : clients) {
            client.send(encodedMessage);
        }
    }

    private synchronized boolean tryStartGame(ClientHandler starter) {
        if (gameStarted) {
            starter.send(new NetworkMessage("SERVER", "Game has already started").encode());
            return false;
        }

        if (clients.size() < MIN_PLAYERS) {
            starter.send(new NetworkMessage("SERVER", "Need at least " + MIN_PLAYERS + " players to start").encode());
            return false;
        }

        gameStarted = true;
        game = new Game(clients.size());
        game.startGame();
        return true;
    }

    private synchronized boolean isGameStarted() {
        return gameStarted && game != null;
    }

    private synchronized void endTurnIfGameStarted(ClientHandler requester) {
        if (!isGameStarted()) {
            requester.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        int expectedPlayerId = game.getCurrentPlayerIndex() + 1;

        if (requester.getPlayerId() != expectedPlayerId) {
            requester.send(new NetworkMessage("SERVER", "It is Player " + expectedPlayerId + "'s turn").encode());
            return;
        }

        game.guiEndTurn();
        sendGameStateToAll();
    }

    private synchronized void sendGameStateTo(ClientHandler client) {
        if (!isGameStarted()) {
            client.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        client.send(new NetworkMessage("GAME_STATE", buildGameStateTextForPlayer(client.getPlayerId())).encode());
    }

    private synchronized void playCardIfValid(ClientHandler requester, String cardNumberText) {
        if (!isGameStarted()) {
            requester.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        int expectedPlayerId = game.getCurrentPlayerIndex() + 1;

        if (requester.getPlayerId() != expectedPlayerId) {
            requester.send(new NetworkMessage("SERVER", "It is Player " + expectedPlayerId + "'s turn").encode());
            return;
        }

        int cardIndex = parseOneBasedCardIndex(cardNumberText);

        if (cardIndex < 0) {
            requester.send(new NetworkMessage("SERVER", "Use PLAY_CARD <number>, for example PLAY_CARD 1").encode());
            return;
        }

        Player player = game.getPlayers().get(requester.getPlayerId() - 1);

        if (cardIndex >= player.getHandCards().size()) {
            requester.send(new NetworkMessage("SERVER", "No hand card number " + (cardIndex + 1)).encode());
            return;
        }

        Card selectedCard = player.getHandCards().get(cardIndex);
        String selectedCardText = cardToText(selectedCard);
        boolean success = game.playCard(selectedCard);

        if (!success) {
            requester.send(new NetworkMessage("SERVER", "Could not play " + selectedCardText).encode());
            return;
        }

        broadcast(new NetworkMessage("BROADCAST", "Player " + requester.getPlayerId() + " played " + selectedCardText));
        sendGameStateToAll();
    }

    private synchronized void discardIfValid(ClientHandler requester, String cardNumberText) {
        if (!isGameStarted()) {
            requester.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        int expectedPlayerId = game.getCurrentPlayerIndex() + 1;

        if (requester.getPlayerId() != expectedPlayerId) {
            requester.send(new NetworkMessage("SERVER", "It is Player " + expectedPlayerId + "'s turn").encode());
            return;
        }

        int cardIndex = parseOneBasedCardIndex(cardNumberText);

        if (cardIndex < 0) {
            requester.send(new NetworkMessage("SERVER", "Use DISCARD <number>, for example DISCARD 1").encode());
            return;
        }

        Player player = game.getPlayers().get(requester.getPlayerId() - 1);

        if (cardIndex >= player.getHandCards().size()) {
            requester.send(new NetworkMessage("SERVER", "No hand card number " + (cardIndex + 1)).encode());
            return;
        }

        Card selectedCard = player.getHandCards().get(cardIndex);
        String selectedCardText = cardToText(selectedCard);

        if (!game.discard(selectedCard)) {
            requester.send(new NetworkMessage("SERVER", "Could not discard " + selectedCardText).encode());
            return;
        }

        broadcast(new NetworkMessage("BROADCAST", "Player " + requester.getPlayerId() + " discarded " + selectedCardText));
        sendGameStateToAll();
    }

    private synchronized void payIfValid(ClientHandler requester, String paymentText) {
        if (!isGameStarted()) {
            requester.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        if (!game.isPaymentSelecting()) {
            requester.send(new NetworkMessage("SERVER", "No payment is required now").encode());
            return;
        }

        Game.PaymentRequest request = game.getCurrentPaymentRequest();
        int payerId = game.getPlayers().indexOf(request.getPayer()) + 1;

        if (requester.getPlayerId() != payerId) {
            requester.send(new NetworkMessage("SERVER", "Player " + payerId + " must pay now").encode());
            return;
        }

        ArrayList<Card> selectedCards = parsePaymentCards(request.getPayer(), paymentText);

        if (selectedCards.isEmpty()) {
            requester.send(new NetworkMessage("SERVER", "Use PAY B1 P2. B means bank card, P means property card").encode());
            return;
        }

        if (!game.finishCurrentPayment(selectedCards)) {
            requester.send(new NetworkMessage("SERVER", "Invalid payment selection").encode());
            return;
        }

        broadcast(new NetworkMessage("BROADCAST", "Player " + requester.getPlayerId() + " paid " + game.getCardsValue(selectedCards) + "M"));
        sendGameStateToAll();
    }

    private synchronized void justSayNoIfValid(ClientHandler requester) {
        if (!isGameStarted()) {
            requester.send(new NetworkMessage("SERVER", "Game has not started yet").encode());
            return;
        }

        if (!game.isPaymentSelecting()) {
            requester.send(new NetworkMessage("SERVER", "No payment is required now").encode());
            return;
        }

        Game.PaymentRequest request = game.getCurrentPaymentRequest();
        int payerId = game.getPlayers().indexOf(request.getPayer()) + 1;

        if (requester.getPlayerId() != payerId) {
            requester.send(new NetworkMessage("SERVER", "Player " + payerId + " must respond now").encode());
            return;
        }

        if (!game.canCurrentPaymentUseJustSayNo()) {
            requester.send(new NetworkMessage("SERVER", "You do not have Just Say No").encode());
            return;
        }

        game.currentPaymentUseJustSayNo();
        broadcast(new NetworkMessage("BROADCAST", "Player " + requester.getPlayerId() + " used Just Say No"));
        sendGameStateToAll();
    }

    private ArrayList<Card> parsePaymentCards(Player payer, String paymentText) {
        ArrayList<Card> selectedCards = new ArrayList<>();
        String[] tokens = paymentText.trim().split("[,\\s]+");

        for (String token : tokens) {
            if (token.length() < 2) {
                continue;
            }

            char source = Character.toUpperCase(token.charAt(0));
            int index = parseOneBasedCardIndex(token.substring(1));

            if (index < 0) {
                continue;
            }

            if (source == 'B' && index < payer.getBankCards().size()) {
                selectedCards.add(payer.getBankCards().get(index));
            } else if (source == 'P' && index < payer.getPropertyCards().size()) {
                selectedCards.add(payer.getPropertyCards().get(index));
            }
        }

        return selectedCards;
    }

    private int parseOneBasedCardIndex(String cardNumberText) {
        try {
            return Integer.parseInt(cardNumberText.trim()) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private synchronized void sendGameStateToAll() {
        if (game == null) {
            return;
        }

        for (ClientHandler client : clients) {
            client.send(new NetworkMessage("GAME_STATE", buildGameStateTextForPlayer(client.getPlayerId())).encode());
        }
    }

    private synchronized String buildGameStateTextForPlayer(int playerId) {
        if (game == null) {
            return "NO_GAME";
        }

        StringBuilder builder = new StringBuilder();
        int playerIndex = playerId - 1;

        builder.append("you=").append(playerId);
        builder.append(";");
        builder.append("currentPlayer=").append(game.getCurrentPlayerIndex() + 1);
        builder.append(";discardPhase=").append(game.isDiscard());
        appendPaymentState(builder, playerId);
        builder.append(";players=");

        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);

            if (i > 0) {
                builder.append(",");
            }

            builder.append("P").append(i + 1);
            builder.append("(hand=").append(player.getHandCards().size());
            builder.append(",bank=").append(player.getBankCards().size());
            builder.append(",properties=").append(player.getPropertyCards().size());
            builder.append(")");
        }

        if (playerIndex >= 0 && playerIndex < game.getPlayers().size()) {
            builder.append(";yourHand=");
            appendCards(builder, game.getPlayers().get(playerIndex).getHandCards());
            builder.append(";yourBank=");
            appendCards(builder, game.getPlayers().get(playerIndex).getBankCards());
            builder.append(";yourProperties=");
            appendProperties(builder, game.getPlayers().get(playerIndex).getPropertyCards());
        }

        return builder.toString();
    }

    private void appendPaymentState(StringBuilder builder, int playerId) {
        builder.append(";payment=");

        if (!game.isPaymentSelecting()) {
            builder.append("none");
            return;
        }

        Game.PaymentRequest request = game.getCurrentPaymentRequest();
        int receiverId = game.getPlayers().indexOf(request.getReceiver()) + 1;
        int payerId = game.getPlayers().indexOf(request.getPayer()) + 1;

        builder.append("payer=").append(payerId);
        builder.append(",receiver=").append(receiverId);
        builder.append(",amount=").append(request.getAmount());

        if (playerId == payerId) {
            builder.append(",youMustPay=true");
            builder.append(",canJustSayNo=").append(game.canCurrentPaymentUseJustSayNo());
        }
    }

    private void appendCards(StringBuilder builder, List<? extends Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append(cardToText(cards.get(i)));
        }
    }

    private void appendProperties(StringBuilder builder, List<PropertiesCards> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append(propertyToText(cards.get(i)));
        }
    }

    private String cardToText(Card card) {
        if (card instanceof MoneyCards) {
            return "MONEY_" + card.getValue();
        }

        if (card instanceof ActionCards actionCard) {
            return "ACTION_" + actionCard.getActionCardType().name() + "_" + card.getValue();
        }

        if (card instanceof PropertiesCards propertyCard) {
            return propertyToText(propertyCard);
        }

        return "CARD_" + card.getValue();
    }

    private String propertyToText(PropertiesCards card) {
        String currentColor = card.getCurrentColor() == null ? "NO_COLOR" : card.getCurrentColor().name();
        return "PROPERTY_" + card.getType().name() + "_" + currentColor + "_" + card.getValue();
    }

    private class ClientHandler extends Thread {
        private final Socket socket;
        private final int playerId;
        private PrintWriter out;

        private ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
        }

        public int getPlayerId() {
            return playerId;
        }

        @Override
        public void run() {
            try (
                    Socket socketToClose = socket;
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out = writer;
                send(new NetworkMessage("WELCOME", "PLAYER " + playerId).encode());
                broadcast(new NetworkMessage("BROADCAST", "Player " + playerId + " joined the game"));
                broadcast(new NetworkMessage("PLAYER_LIST", getPlayerListText()));

                String line;

                while ((line = in.readLine()) != null) {
                    NetworkMessage message = NetworkMessage.decode(line);
                    System.out.println("From Player " + playerId + ": " + message.getType() + " " + message.getBody());
                    handleMessage(message);
                }
            } catch (IOException e) {
                System.out.println("Connection error for Player " + playerId + ": " + e.getMessage());
            } finally {
                removeClient(this);
                broadcast(new NetworkMessage("BROADCAST", "Player " + playerId + " left the game"));
                broadcast(new NetworkMessage("PLAYER_LIST", getPlayerListText()));
            }
        }

        private void handleMessage(NetworkMessage message) {
            if ("HELLO".equals(message.getType())) {
                broadcast(new NetworkMessage("BROADCAST", "Player " + playerId + " says hello"));
            } else if ("PLAYERS".equals(message.getType())) {
                send(new NetworkMessage("PLAYER_LIST", getPlayerListText()).encode());
            } else if ("STATE".equals(message.getType())) {
                sendGameStateTo(this);
            } else if ("START_GAME".equals(message.getType())) {
                if (tryStartGame(this)) {
                    broadcast(new NetworkMessage(
                            "GAME_STARTED",
                            "Started with " + getPlayerCount() + " players: " + getPlayerListText()
                    ));
                    sendGameStateToAll();
                }
            } else if ("PLAY_CARD".equals(message.getType())) {
                playCardIfValid(this, message.getBody());
            } else if ("DISCARD".equals(message.getType())) {
                discardIfValid(this, message.getBody());
            } else if ("PAY".equals(message.getType())) {
                payIfValid(this, message.getBody());
            } else if ("JUST_SAY_NO".equals(message.getType())) {
                justSayNoIfValid(this);
            } else if ("END_TURN".equals(message.getType())) {
                endTurnIfGameStarted(this);
            } else {
                send(new NetworkMessage("SERVER", "Unknown message: " + message.getType()).encode());
            }
        }

        private void send(String encodedMessage) {
            if (out != null) {
                out.println(encodedMessage);
            }
        }
    }
}
