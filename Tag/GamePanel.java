package Arcade.Tag;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import Arcade.ArcadeMain;

public class GamePanel extends JPanel implements ActionListener {
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 550; // Adjusted height to fit within the new frame size
    private static final int DELAY = 10;
    private static final int PLAYER_SIZE = 20;

    private Timer timer;
    private Player player;
    private List<AIPlayer> aiPlayers;
    private boolean paused;
    private JLabel narratorLabel;

    public GamePanel() {
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
        player = new Player(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
        aiPlayers = new ArrayList<>();
        aiPlayers.add(new AIPlayer(100, 100));
        aiPlayers.add(new AIPlayer(700, 100));
        aiPlayers.add(new AIPlayer(100, 400));
        aiPlayers.add(new AIPlayer(700, 400));
        paused = true; // Start game paused

        // Narrator panel
        narratorLabel = new JLabel("Welcome to the game of Tag!", SwingConstants.CENTER);
        narratorLabel.setFont(new Font("Serif", Font.BOLD, 18));
        narratorLabel.setForeground(Color.PINK);
        this.setLayout(new BorderLayout());
        this.add(narratorLabel, BorderLayout.NORTH);
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.setDY(-5);
        }
        if (key == KeyEvent.VK_S) {
            player.setDY(5);
        }
        if (key == KeyEvent.VK_A) {
            player.setDX(-5);
        }
        if (key == KeyEvent.VK_D) {
            player.setDX(5);
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
    }

    private void updateAIPlayers() {
        for (AIPlayer aiPlayer : aiPlayers) {
            aiPlayer.chase(player);
            aiPlayer.update();
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
