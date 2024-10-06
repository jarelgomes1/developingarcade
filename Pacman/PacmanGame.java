package Arcade.Pacman;

import javax.swing.*;
import java.awt.*;

public class PacmanGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        PacmanBoard board = new PacmanBoard();

        // Back to Menu, Restart, Quit, Pause, and Resume buttons
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            Arcade.ArcadeMain.showMainMenu();
        });
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> board.restartGame());
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> board.pauseGame());
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> board.resumeGame());

        buttonPanel.add(backButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);

        frame.add(board, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        board.startGame();
    }
}
