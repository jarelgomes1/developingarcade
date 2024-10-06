package Arcade.AgarIo;

import javax.swing.*;
import java.awt.*;
import Arcade.ArcadeMain; // Ensure this import is added

public class AgarIoGame {
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Agar.io");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 650); // Adjusted size to accommodate bottom belt
            frame.setLayout(new BorderLayout());

            // Create game panel
            gamePanel = new GamePanel();

            // Add Restart, Quit, and other control buttons
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> gamePanel.restartGame());

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> System.exit(0));

            JButton menuButton = new JButton("Back to Menu");
            menuButton.addActionListener(e -> {
                frame.dispose();
                ArcadeMain.main(null);
            });

            JButton pauseButton = new JButton("Pause");
            pauseButton.addActionListener(e -> gamePanel.pauseGame());

            JButton playButton = new JButton("Play");
            playButton.addActionListener(e -> gamePanel.resumeGame());

            bottomPanel.add(restartButton);
            bottomPanel.add(menuButton);
            bottomPanel.add(pauseButton);
            bottomPanel.add(playButton);
            bottomPanel.add(quitButton);

            frame.add(gamePanel, BorderLayout.CENTER);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            // Set frame visible
            frame.setVisible(true);

            // Show start prompt
            gamePanel.showStartPrompt();
        });
    }
}
