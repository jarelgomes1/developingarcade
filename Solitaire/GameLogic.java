package Arcade.Solitaire;

import javax.swing.*;

public class GameLogic {
    private Deck deck;
    private PlayerPanel[] players;
    private int currentPlayerIndex;
    private boolean isPaused;

    public GameLogic(PlayerPanel[] players) {
        this.players = players;
        this.deck = new Deck();
        this.currentPlayerIndex = 0;
        this.isPaused = false;
    }

    public void startGame() {
        distributeCards();
        notifyCurrentPlayer();
    }

    private void distributeCards() {
        for (PlayerPanel player : players) {
            for (int i = 0; i < 5; i++) { // Example: give 5 cards to each player
                Card card = deck.drawCard();
                player.addCard(card);
            }
        }
    }

    private void notifyCurrentPlayer() {
        for (int i = 0; i < players.length; i++) {
            if (i == currentPlayerIndex) {
                players[i].showDeck();
                players[i].requestFocusInWindow();
            } else {
                players[i].hideDeck();
            }
        }
        SolitaireGame.updateNarrator("It's " + players[currentPlayerIndex].getName() + "'s turn.");
        promptMove(players[currentPlayerIndex]);
    }

    private void promptMove(PlayerPanel player) {
        // Extend options to include "Show Deck" buttons for all players
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton drawCardButton = new JButton("Draw Card");
        drawCardButton.addActionListener(e -> drawCard(player));

        JButton playCardButton = new JButton("Play Card");
        playCardButton.addActionListener(e -> selectCardToPlay(player));

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.addActionListener(e -> backToMenu());

        panel.add(drawCardButton);
        panel.add(playCardButton);
        panel.add(backToMenuButton);

        for (PlayerPanel p : players) {
            JButton showDeckButton = new JButton("Show " + p.getName() + "'s Deck");
            showDeckButton.addActionListener(e -> p.showDeck());
            panel.add(showDeckButton);
        }

        JOptionPane.showMessageDialog(null, panel, player.getName() + ", choose your move:", JOptionPane.PLAIN_MESSAGE);
    }

    private void selectCardToPlay(PlayerPanel player) {
        if (player.getHandSize() > 0) {
            player.showDeck(); // Ensure the deck is visible for selection
            String[] cardOptions = new String[player.getHandSize()];
            for (int i = 0; i < player.getHandSize(); i++) {
                cardOptions[i] = player.getCard(i).toString();
            }

            String selectedCardStr = (String) JOptionPane.showInputDialog(null,
                    "Select a card to play:",
                    "Select Card",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    cardOptions,
                    cardOptions[0]);

            if (selectedCardStr != null) {
                for (int i = 0; i < player.getHandSize(); i++) {
                    if (player.getCard(i).toString().equals(selectedCardStr)) {
                        playCard(player, player.getCard(i));
                        return;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No cards to play. Drawing a card instead.");
            drawCard(player);
        }
    }

    public void nextTurn() {
        if (deck.getSize() == 0 && allHandsEmpty()) {
            endGame(null); // End the game in a draw if the deck is empty and no player can play
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        notifyCurrentPlayer();
    }

    public void playCard(PlayerPanel player, Card card) {
        SolitaireGame.updateNarrator(player.getName() + " plays " + card);
        player.removeCard(card);
        checkVictoryConditions(player);
        if (deck.getSize() > 0 || player.getHandSize() > 0) {
            nextTurn();
        } else {
            endGame(null); // End the game in a draw if the deck is empty and no player can play
        }
    }

    public void drawCard(PlayerPanel player) {
        Card card = deck.drawCard();
        if (card != null) {
            player.addCard(card);
            SolitaireGame.updateNarrator(player.getName() + " draws a card: " + card);
            nextTurn();
        } else {
            SolitaireGame.updateNarrator("The deck is empty.");
            nextTurn();
        }
    }

    private boolean allHandsEmpty() {
        for (PlayerPanel player : players) {
            if (player.getHandSize() > 0) {
                return false;
            }
        }
        return true;
    }

    private void checkVictoryConditions(PlayerPanel player) {
        if (player.getHandSize() == 0) {
            endGame(player);
        }
    }

    private void endGame(PlayerPanel winner) {
        if (winner != null) {
            SolitaireGame.updateNarrator(winner.getName() + " wins!");
            JOptionPane.showMessageDialog(null, winner.getName() + " wins!");
        } else {
            SolitaireGame.updateNarrator("The game ends in a draw!");
            JOptionPane.showMessageDialog(null, "The game ends in a draw!");
        }
        int choice = JOptionPane.showOptionDialog(null,
                "Game Over. What would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Back to Menu" },
                "Restart");
        if (choice == 0) {
            restartGame();
        } else {
            backToMenu();
        }
    }

    public void pauseGame() {
        isPaused = true;
        SolitaireGame.updateNarrator("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        SolitaireGame.updateNarrator("Game resumed.");
    }

    private void restartGame() {
        deck = new Deck();
        currentPlayerIndex = 0;
        for (PlayerPanel player : players) {
            player.clearHand();
        }
        startGame();
    }

    private void backToMenu() {
        System.exit(0); // For simplicity, we'll just exit the application
    }
}
