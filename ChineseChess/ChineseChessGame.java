package Arcade.ChineseChess;

import javax.swing.*;
import java.awt.*;

public class ChineseChessGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chinese Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // Increased size to accommodate the side panel

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Side panel for captured pieces
        JPanel capturedRedPanel = new JPanel();
        JPanel capturedBlackPanel = new JPanel();
        ChineseChessBoard board = new ChineseChessBoard(capturedRedPanel, capturedBlackPanel);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 600));
        sidePanel.add(board.createCapturedPiecesPanel(), BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        mainPanel.add(board, BorderLayout.CENTER);

        // Button panel with Back to Menu, Restart, Quit, Pause, and Resume buttons
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            Arcade.ArcadeMain.showMainMenu();
        });
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> board.initializeBoard());
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
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
