package Arcade;

import javax.swing.*;
import Arcade.WesternChess.ChessGame;
import Arcade.TicTacToe.TicTacToeGame;
import Arcade.CryptoZoo.CryptoZooGame;
import Arcade.ChineseChess.ChineseChessGame;
import Arcade.Tetris.TetrisGame;
import Arcade.Pacman.PacmanGame;
import Arcade.FlappyBird.FlappyBirdGame;
import Arcade.Solitaire.SolitaireGame;
import Arcade.Checkers.CheckersGame;
import Arcade.ThreePlayerChess.ThreePlayerChessGame;
import Arcade.Pong.PongGame;
import Arcade.Tag.TagGame;
import Arcade.MazeTag.MazeTagGame;
import Arcade.AgarIo.AgarIoGame;
import Arcade.LofiLounge.LofiLounge;
import java.awt.*;

public class ArcadeMain {
    public static void main(String[] args) {
        // Launch the main menu
        SwingUtilities.invokeLater(() -> showMainMenu());
    }

    public static void showMainMenu() {
        // Create the main frame
        JFrame frame = new JFrame("Jarel's Arcade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Welcome panel with a cute futuristic look
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.BLACK);
        welcomePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Jarel's Arcade", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.PINK);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new GridLayout(0, 4, 10, 10)); // 0 rows, 4 columns, 10px hgap, 10px vgap

        // Add sections with game buttons
        addSection(buttonPanel, "Chess Games", new JButton[] {
                createGameButton("Western Chess", frame, () -> ChessGame.main(null)),
                createGameButton("Chinese Chess", frame, () -> ChineseChessGame.main(null)),
                createGameButton("Three-Player Chess", frame, () -> ThreePlayerChessGame.main(null)),
                createGameButton("Checkers", frame, () -> CheckersGame.main(null))
        });

        addSection(buttonPanel, "Card Games", new JButton[] {
                createGameButton("Solitaire", frame, () -> SolitaireGame.main(null))
        });

        addSection(buttonPanel, "Classic Games", new JButton[] {
                createGameButton("Tic-Tac-Toe", frame, () -> TicTacToeGame.main(null)),
                createGameButton("Tetris", frame, () -> TetrisGame.main(null)),
                createGameButton("Pac-Man", frame, () -> PacmanGame.main(null)),
                createGameButton("Pong", frame, () -> PongGame.main(null))
        });

        addSection(buttonPanel, "Action Games", new JButton[] {
                createGameButton("Flappy Bird", frame, () -> FlappyBirdGame.main(null)),
                createGameButton("Tag", frame, () -> TagGame.main(null)),
                createGameButton("Maze Tag", frame, () -> MazeTagGame.main(null)),
                createGameButton("Agar.io", frame, () -> AgarIoGame.main(null))
        });

        addSection(buttonPanel, "Adventure Games", new JButton[] {
                createGameButton("CryptoZoo", frame, () -> CryptoZooGame.main(null))
        });

        addSection(buttonPanel, "Relaxation", new JButton[] {
                createGameButton("Lofi Lounge", frame, () -> LofiLounge.main(null))
        });

        // Add welcome panel and button panel to the frame
        frame.add(welcomePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    private static void addSection(JPanel buttonPanel, String sectionTitle, JButton[] buttons) {
        JLabel sectionLabel = new JLabel(sectionTitle, SwingConstants.CENTER);
        sectionLabel.setFont(new Font("Serif", Font.BOLD, 18));
        sectionLabel.setForeground(Color.PINK);
        buttonPanel.add(sectionLabel);
        buttonPanel.add(new JLabel("")); // Placeholder for alignment
        buttonPanel.add(new JLabel("")); // Placeholder for alignment
        buttonPanel.add(new JLabel("")); // Placeholder for alignment

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        int placeholders = 4 - (buttons.length % 4);
        if (placeholders != 4) {
            for (int i = 0; placeholders > 0 && i < placeholders; i++) {
                buttonPanel.add(new JLabel("")); // Placeholder to maintain 4-column layout
            }
        }
    }

    private static JButton createGameButton(String title, JFrame frame, Runnable gameLauncher) {
        JButton button = new JButton(title);
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.PINK);
        button.setFocusPainted(false);
        button.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            frame.dispose();
            gameLauncher.run();
        }));
        return button;
    }
}
