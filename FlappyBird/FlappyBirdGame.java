package Arcade.FlappyBird;

import javax.swing.*;
import java.awt.*;

public class FlappyBirdGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Start screen panel
        JPanel startPanel = new JPanel();
        startPanel.setBackground(Color.CYAN);
        startPanel.setLayout(new BorderLayout());

        JLabel startLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
        startLabel.setFont(new Font("Serif", Font.BOLD, 36));
        startLabel.setForeground(Color.ORANGE);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Serif", Font.BOLD, 24));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.ORANGE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().invalidate();
            showGameScreen(frame);
            frame.getContentPane().revalidate();
        });

        startPanel.add(startLabel, BorderLayout.CENTER);
        startPanel.add(startButton, BorderLayout.SOUTH);

        frame.add(startPanel);
        frame.setVisible(true);
    }

    private static void showGameScreen(JFrame frame) {
        JPanel buttonPanel = new JPanel(); // Create the button panel
        FlappyBirdPanel gamePanel = new FlappyBirdPanel(frame, buttonPanel); // Pass it to the FlappyBirdPanel
                                                                             // constructor

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        gamePanel.startGame();
    }
}
