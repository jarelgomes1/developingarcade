package Arcade.WesternChess;

import javax.swing.*;
import java.awt.*;
import Arcade.ArcadeMain;

public class ChessGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 600));

        JLabel turnLabel = new JLabel("Turn: White");
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 16));
        sidePanel.add(turnLabel, BorderLayout.NORTH);

        JLabel statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 16));
        sidePanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel capturedPanel = new JPanel(new GridLayout(2, 1));
        JPanel capturedPanelWhite = new JPanel();
        JPanel capturedPanelBlack = new JPanel();
        capturedPanelWhite.setBorder(BorderFactory.createTitledBorder("Captured by Black"));
        capturedPanelBlack.setBorder(BorderFactory.createTitledBorder("Captured by White"));
        capturedPanel.add(capturedPanelWhite);
        capturedPanel.add(capturedPanelBlack);
        sidePanel.add(capturedPanel, BorderLayout.CENTER);

        ChessBoard board = new ChessBoard(turnLabel, statusLabel, capturedPanelWhite, capturedPanelBlack);
        ChessBoard finalBoard = board;

        mainPanel.add(board, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            finalBoard.initialize();
            finalBoard.updateBoard();
        });
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        JButton pauseButton = new JButton("Pause");
        JButton playButton = new JButton("Play");
        JButton backButton = new JButton("Back to Menu"); // Added back button

        backButton.addActionListener(e -> {
            frame.dispose();
            ArcadeMain.showMainMenu();
        });

        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(playButton);
        buttonPanel.add(backButton); // Added back button to button panel

        sidePanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
