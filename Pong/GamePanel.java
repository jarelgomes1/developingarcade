package Arcade.Pong;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import Arcade.ArcadeMain; // Ensure this import is added

public class GamePanel extends JPanel implements ActionListener {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 550; // Adjusted height to fit within the new frame size
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int DELAY = 10;
    private static final int MAX_LEVEL = 10;

    private Timer timer;
    private int leftPaddleY, rightPaddleY;
    private int leftPaddleDY, rightPaddleDY;
    private boolean paused;
    private List<Ball> balls;
    private int hitCount;
    private int level;

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
        leftPaddleY = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        rightPaddleY = PANEL_HEIGHT / 2 - PADDLE_HEIGHT / 2;
        leftPaddleDY = 0;
        rightPaddleDY = 0;
        paused = true; // Start game paused
        balls = new ArrayList<>();
        balls.add(new Ball());
        hitCount = 0;
        level = 1;
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            leftPaddleDY = -5;
        }
        if (key == KeyEvent.VK_S) {
            leftPaddleDY = 5;
        }
        if (key == KeyEvent.VK_UP) {
            rightPaddleDY = -5;
        }
        if (key == KeyEvent.VK_DOWN) {
            rightPaddleDY = 5;
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            leftPaddleDY = 0;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            rightPaddleDY = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            updatePaddles();
            updateBalls();
            checkCollisions();
        }
        repaint();
    }

    private void updatePaddles() {
        leftPaddleY += leftPaddleDY;
        rightPaddleY += rightPaddleDY;

        if (leftPaddleY < 0) {
            leftPaddleY = 0;
        }
        if (leftPaddleY > PANEL_HEIGHT - PADDLE_HEIGHT) {
            leftPaddleY = PANEL_HEIGHT - PADDLE_HEIGHT;
        }
        if (rightPaddleY < 0) {
            rightPaddleY = 0;
        }
        if (rightPaddleY > PANEL_HEIGHT - PADDLE_HEIGHT) {
            rightPaddleY = PANEL_HEIGHT - PADDLE_HEIGHT;
        }
    }

    private void updateBalls() {
        for (Ball ball : balls) {
            ball.update();
        }
    }

    private void checkCollisions() {
        for (Ball ball : balls) {
            if (ball.checkCollision(leftPaddleY, rightPaddleY)) {
                hitCount++;
                ball.incrementSpeed();
                if (hitCount % 8 == 0) {
                    level++;
                    if (level > MAX_LEVEL) {
                        showConcedePrompt();
                        return;
                    } else {
                        balls.add(new Ball());
                        showNewLevelPrompt(level);
                        return;
                    }
                }
            }

            if (ball.x < 0) {
                JOptionPane.showMessageDialog(this, "Right Player Wins!");
                restartGame();
                break;
            }
            if (ball.x > PANEL_WIDTH - BALL_SIZE) {
                JOptionPane.showMessageDialog(this, "Left Player Wins!");
                restartGame();
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, leftPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(PANEL_WIDTH - PADDLE_WIDTH, rightPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        for (Ball ball : balls) {
            ball.draw(g);
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

    private void showConcedePrompt() {
        int choice = JOptionPane.showOptionDialog(this,
                "Game Concedes! What would you like to do?",
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

    private void showNewLevelPrompt(int level) {
        int choice = JOptionPane.showOptionDialog(this,
                "New Level! You're now at level " + level,
                "New Level",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "OK" },
                "OK");
        if (choice == 0) {
            resumeGame();
        }
    }

    private class Ball {
        private int x, y, dx, dy;
        private double speed;

        public Ball() {
            x = PANEL_WIDTH / 2 - BALL_SIZE / 2;
            y = PANEL_HEIGHT / 2 - BALL_SIZE / 2;
            dx = (Math.random() > 0.5 ? 1 : -1) * 2;
            dy = (Math.random() > 0.5 ? 1 : -1) * 2;
            speed = 1.0;
        }

        public void update() {
            x += dx * speed;
            y += dy * speed;

            if (y < 0 || y > PANEL_HEIGHT - BALL_SIZE) {
                dy = -dy;
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillOval(x, y, BALL_SIZE, BALL_SIZE);
        }

        public boolean checkCollision(int leftPaddleY, int rightPaddleY) {
            boolean collision = false;

            if (x < PADDLE_WIDTH && y + BALL_SIZE > leftPaddleY && y < leftPaddleY + PADDLE_HEIGHT) {
                dx = -dx;
                x = PADDLE_WIDTH;
                collision = true;
            }
            if (x > PANEL_WIDTH - PADDLE_WIDTH - BALL_SIZE && y + BALL_SIZE > rightPaddleY
                    && y < rightPaddleY + PADDLE_HEIGHT) {
                dx = -dx;
                x = PANEL_WIDTH - PADDLE_WIDTH - BALL_SIZE;
                collision = true;
            }

            return collision;
        }

        public void incrementSpeed() {
            speed += 0.05; // Make the speed increment more gradual
        }
    }
}
