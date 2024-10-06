package Arcade.Checkers;

import javax.swing.*;
import java.awt.*;

public class CheckersGame {
    private static JLabel capturedWhitePiecesLabel;
    private static JLabel capturedBlackPiecesLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Checkers");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLayout(new BorderLayout());

            // Create board
            Board board = new Board();

            // Create side panel for captured pieces and game info
            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setPreferredSize(new Dimension(200, frame.getHeight()));

            // Captured pieces labels
            capturedWhitePiecesLabel = new JLabel("Captured White Pieces: 0");
            capturedBlackPiecesLabel = new JLabel("Captured Black Pieces: 0");

            sidePanel.add(capturedWhitePiecesLabel);
            sidePanel.add(capturedBlackPiecesLabel);

            // Add Restart and Quit buttons
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> restartGame(frame));

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> System.exit(0));

            bottomPanel.add(restartButton);
            bottomPanel.add(quitButton);

            frame.add(board, BorderLayout.CENTER);
            frame.add(sidePanel, BorderLayout.EAST);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    private static void restartGame(JFrame frame) {
        frame.dispose();
        main(null);
    }

    public static void updateCapturedPieces(int whitePieces, int blackPieces) {
        capturedWhitePiecesLabel.setText("Captured White Pieces: " + whitePieces);
        capturedBlackPiecesLabel.setText("Captured Black Pieces: " + blackPieces);
    }

    public static void showWinPrompt(String winner) {
        int choice = JOptionPane.showOptionDialog(null,
                winner + " wins! What would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Quit" },
                "Restart");
        if (choice == 0) {
            restartGame((JFrame) SwingUtilities.getWindowAncestor(capturedWhitePiecesLabel));
        } else {
            System.exit(0);
        }
    }
}
