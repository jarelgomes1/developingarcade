package Arcade.MazeTag;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Arcade.ArcadeMain;

public class MazeGamePanel extends JPanel implements ActionListener {
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 550;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 10;
    private static final int PLAYER_SIZE = 20;

    private Timer timer;
    private Player player;
    private List<AIPlayer> aiPlayers;
    private boolean paused;
    private JLabel narratorLabel;
    private int[][] maze;

    public MazeGamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setBorder(new LineBorder(Color.WHITE)); // Add a white border to the game panel
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        initGame();
        timer = new Timer(DELAY, this);
    }

    private void initGame() {
        maze = generateMaze(PANEL_WIDTH / CELL_SIZE, PANEL_HEIGHT / CELL_SIZE);
        player = new Player(CELL_SIZE, CELL_SIZE);
        aiPlayers = new ArrayList<>();
        aiPlayers.add(new AIPlayer(CELL_SIZE * 3, CELL_SIZE * 3, maze));
        aiPlayers.add(new AIPlayer(CELL_SIZE * 15, CELL_SIZE * 3, maze));
        aiPlayers.add(new AIPlayer(CELL_SIZE * 3, CELL_SIZE * 15, maze));
        aiPlayers.add(new AIPlayer(CELL_SIZE * 15, CELL_SIZE * 15, maze));
        paused = true; // Start game paused

        // Narrator panel
        narratorLabel = new JLabel("Welcome to the maze tag game!", SwingConstants.CENTER);
        narratorLabel.setFont(new Font("Serif", Font.BOLD, 18));
        narratorLabel.setForeground(Color.PINK);
        this.setLayout(new BorderLayout());
        this.add(narratorLabel, BorderLayout.NORTH);
    }

    private int[][] generateMaze(int width, int height) {
        int[][] maze = new int[width][height];
        Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze[x][y] = random.nextInt(4) == 0 ? 1 : 0;
            }
        }

        maze[1][1] = 0;
        return maze;
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.setDY(-CELL_SIZE);
        }
        if (key == KeyEvent.VK_S) {
            player.setDY(CELL_SIZE);
        }
        if (key == KeyEvent.VK_A) {
            player.setDX(-CELL_SIZE);
        }
        if (key == KeyEvent.VK_D) {
            player.setDX(CELL_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            updatePlayer();
            updateAIPlayers();
            checkCollisions();
        }
        repaint();
    }

    private void updatePlayer() {
        player.update();
        if (maze[player.getX() / CELL_SIZE][player.getY() / CELL_SIZE] == 1) {
            player.undoMove();
        }
    }

    private void updateAIPlayers() {
        for (AIPlayer aiPlayer : aiPlayers) {
            aiPlayer.chase(player);
            aiPlayer.update();
            if (maze[aiPlayer.getX() / CELL_SIZE][aiPlayer.getY() / CELL_SIZE] == 1) {
                aiPlayer.undoMove();
            }
        }
    }

    private void checkCollisions() {
        for (AIPlayer aiPlayer : aiPlayers) {
            if (aiPlayer.getBounds().intersects(player.getBounds())) {
                narratorLabel.setText("You got tagged by an AI player!");
                showTaggedPrompt();
                pauseGame();
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the maze
        for (int y = 0; y < maze[0].length; y++) {
            for (int x = 0; x < maze.length; x++) {
                if (maze[x][y] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        player.draw(g);
        for (AIPlayer aiPlayer : aiPlayers) {
            aiPlayer.draw(g);
        }
    }

    public void restartGame() {
        initGame();
        requestFocusInWindow();
    }

    public void pauseGame() {
        paused = true;
    }

    public void resumeGame() {
        paused = false;
        timer.start();
        requestFocusInWindow();
    }

    public void showStartPrompt() {
        int choice = JOptionPane.showOptionDialog(this,
                "Press 'Play' to start the game.",
                "Start Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Play" },
                "Play");
        if (choice == 0) {
            resumeGame();
        }
    }

    private void showTaggedPrompt() {
        int choice = JOptionPane.showOptionDialog(this,
                "You got tagged! What would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Back to Menu" },
                "Restart");
        if (choice == 0) {
            restartGame();
        } else {
            ArcadeMain.main(null);
        }
    }
}
