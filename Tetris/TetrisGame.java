package Arcade.Tetris;

import javax.swing.*;
import java.awt.*;

public class TetrisGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 800);

        TetrisBoard board = new TetrisBoard();

        // Button panel with Restart, Quit, Pause, and Resume buttons
        JPanel buttonPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> board.restartGame());
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> board.pauseGame());
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> board.resumeGame());

        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);

        frame.add(board, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
