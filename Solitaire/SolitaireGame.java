package Arcade.Solitaire;

import javax.swing.*;
import java.awt.*;

public class SolitaireGame {
    private static JTextArea gameNarrator;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Four-Player Solitaire");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 600);
            frame.setLayout(new BorderLayout());

            PlayerPanel[] playerPanels = new PlayerPanel[4];
            JPanel gamePanel = new JPanel(new GridLayout(2, 2));
            for (int i = 0; i < 4; i++) {
                playerPanels[i] = new PlayerPanel(i + 1);
                gamePanel.add(playerPanels[i]);
            }

            frame.add(gamePanel, BorderLayout.CENTER);

            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

            JButton backToMenuButton = new JButton("Back to Menu");
            backToMenuButton.addActionListener(e -> backToMenu());

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> restartGame(frame, playerPanels));

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> quitGame());

            JButton pauseButton = new JButton("Pause");
            pauseButton.addActionListener(e -> pauseGame());

            JButton playButton = new JButton("Play");
            playButton.addActionListener(e -> playGame());

            sidePanel.add(backToMenuButton);
            sidePanel.add(restartButton);
            sidePanel.add(quitButton);
            sidePanel.add(pauseButton);
            sidePanel.add(playButton);

            frame.add(sidePanel, BorderLayout.EAST);

            gameNarrator = new JTextArea();
            gameNarrator.setEditable(false);
            gameNarrator.setLineWrap(true);
            gameNarrator.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(gameNarrator);
            scrollPane.setPreferredSize(new Dimension(200, frame.getHeight()));
            frame.add(scrollPane, BorderLayout.WEST);

            frame.setVisible(true);

            GameLogic gameLogic = new GameLogic(playerPanels);
            gameLogic.startGame();
        });
    }

    private static void backToMenu() {
        // Implementation for going back to menu
    }

    private static void restartGame(JFrame frame, PlayerPanel[] playerPanels) {
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            JFrame newFrame = new JFrame("Four-Player Solitaire");
            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newFrame.setSize(1200, 600);
            newFrame.setLayout(new BorderLayout());

            PlayerPanel[] newPlayerPanels = new PlayerPanel[4];
            JPanel newGamePanel = new JPanel(new GridLayout(2, 2));
            for (int i = 0; i < 4; i++) {
                newPlayerPanels[i] = new PlayerPanel(i + 1);
                newGamePanel.add(newPlayerPanels[i]);
            }

            newFrame.add(newGamePanel, BorderLayout.CENTER);

            JPanel newSidePanel = new JPanel();
            newSidePanel.setLayout(new BoxLayout(newSidePanel, BoxLayout.Y_AXIS));

            JButton backToMenuButton = new JButton("Back to Menu");
            backToMenuButton.addActionListener(e -> backToMenu());

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> restartGame(newFrame, newPlayerPanels));

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> quitGame());

            JButton pauseButton = new JButton("Pause");
            pauseButton.addActionListener(e -> pauseGame());

            JButton playButton = new JButton("Play");
            playButton.addActionListener(e -> playGame());

            newSidePanel.add(backToMenuButton);
            newSidePanel.add(restartButton);
            newSidePanel.add(quitButton);
            newSidePanel.add(pauseButton);
            newSidePanel.add(playButton);

            newFrame.add(newSidePanel, BorderLayout.EAST);

            JTextArea newGameNarrator = new JTextArea();
            newGameNarrator.setEditable(false);
            newGameNarrator.setLineWrap(true);
            newGameNarrator.setWrapStyleWord(true);
            JScrollPane newScrollPane = new JScrollPane(newGameNarrator);
            newScrollPane.setPreferredSize(new Dimension(200, newFrame.getHeight()));
            newFrame.add(newScrollPane, BorderLayout.WEST);

            newFrame.setVisible(true);

            GameLogic newGameLogic = new GameLogic(newPlayerPanels);
            newGameLogic.startGame();
        });
    }

    private static void quitGame() {
        System.exit(0);
    }

    private static void pauseGame() {
        // Implementation for pausing the game
    }

    private static void playGame() {
        // Implementation for resuming the game
    }

    public static void updateNarrator(String message) {
        gameNarrator.append(message + "\n");
    }
}
