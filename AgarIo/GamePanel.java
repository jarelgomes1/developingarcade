package Arcade.AgarIo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Arcade.ArcadeMain;

public class GamePanel extends JPanel implements ActionListener {
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 550; // Adjusted height to fit within the new frame size
    private static final int DELAY = 10;

    private Timer timer;
    private Player player;
    private List<AIPlayer> aiPlayers;
    private boolean paused;

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

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });

        initGame();
        timer = new Timer(DELAY, this);
    }

    private void initGame() {
        player = new Player(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
        aiPlayers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            aiPlayers.add(new AIPlayer(new Random().nextInt(PANEL_WIDTH), new Random().nextInt(PANEL_HEIGHT)));
        }
        paused = true; // Start game paused
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.setDY(-1);
        }
        if (key == KeyEvent.VK_S) {
            player.setDY(1);
        }
        if (key == KeyEvent.VK_A) {
            player.setDX(-1);
        }
        if (key == KeyEvent.VK_D) {
            player.setDX(1);
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            player.setDY(0);
        }
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
            player.setDX(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            player.update();
            for (AIPlayer aiPlayer : aiPlayers) {
                aiPlayer.chase(player);
                aiPlayer.update();
            }
            checkCollisions();
        }
        repaint();
    }

    private void checkCollisions() {
        List<AIPlayer> toRemove = new ArrayList<>();
        for (AIPlayer aiPlayer : aiPlayers) {
            if (player.getBounds().intersects(aiPlayer.getBounds())) {
                if (player.getSize() > aiPlayer.getSize()) {
                    player.grow(aiPlayer.getSize() / 2);
                    toRemove.add(aiPlayer);
                } else {
                    showGameOverPrompt();
                    return;
                }
            }
            for (AIPlayer otherAI : aiPlayers) {
                if (aiPlayer != otherAI && aiPlayer.getBounds().intersects(otherAI.getBounds())) {
                    if (aiPlayer.getSize() > otherAI.getSize()) {
                        aiPlayer.grow(otherAI.getSize() / 2);
                        toRemove.add(otherAI);
                    }
                }
            }
        }
        aiPlayers.removeAll(toRemove);
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

    private void showGameOverPrompt() {
        int choice = JOptionPane.showOptionDialog(this,
                "You reached the end! What would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Back to Menu" },
                "Restart");
        if (choice == 0) {
            restartGame();
        } else {
            ArcadeMain.main(null); // Ensure this import is added
        }
    }
}
