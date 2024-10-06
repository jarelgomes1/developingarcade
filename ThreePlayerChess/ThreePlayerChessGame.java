package Arcade.ThreePlayerChess;

import javax.swing.*;
import java.awt.*;

public class ThreePlayerChessGame {
    private static JLabel capturedPiecesLabel;
    private static Board board;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Three-Player Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLayout(new BorderLayout());

            // Create board
            board = new Board();

            // Create side panel for captured pieces and game info
            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setPreferredSize(new Dimension(200, frame.getHeight()));

            // Captured pieces label
            capturedPiecesLabel = new JLabel("Captured Pieces: 0");
            sidePanel.add(capturedPiecesLabel);

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

    public static void updateCapturedPieces(int capturedPieces) {
        capturedPiecesLabel.setText("Captured Pieces: " + capturedPieces);
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
            restartGame((JFrame) SwingUtilities.getWindowAncestor(capturedPiecesLabel));
        } else {
            System.exit(0);
        }
    }
}
