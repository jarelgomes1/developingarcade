package Arcade.FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class FlappyBirdPanel extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int GROUND_HEIGHT = 50;
    private final int PIPE_WIDTH = 100;
    private final int PIPE_GAP = 200;
    private final int PIPE_INTERVAL = 300;
    private final int BIRD_SIZE = 40;

    private Timer timer;
    private int birdX, birdY, birdVelocity;
    private List<Rectangle> pipes;
    private boolean isPaused = false;
    private int score;
    private int highScore = 0;
    private PriorityQueue<ScoreEntry> leaderboard;

    public FlappyBirdPanel(JFrame frame, JPanel buttonPanel) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    birdVelocity = -10;
                }
            }
        });

        initGame();
        addBottomButtons(frame, buttonPanel);
    }

    private void addBottomButtons(JFrame frame, JPanel buttonPanel) {
        buttonPanel.removeAll();

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            Arcade.ArcadeMain.showMainMenu();
        });

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> pauseGame());

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> resumeGame());

        buttonPanel.add(backButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
    }

    private void initGame() {
        birdX = WIDTH / 4;
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipes = new ArrayList<>();
        score = 0;
        for (int i = 0; i < 5; i++) {
            addPipe(i * PIPE_INTERVAL + WIDTH);
        }

        timer = new Timer(20, this);

        leaderboard = new PriorityQueue<>((a, b) -> b.score - a.score);
    }

    public void startGame() {
        timer.start();
    }

    public void restartGame() {
        timer.stop();
        if (score > highScore) {
            highScore = score;
        }
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "New High Score!",
                JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            leaderboard.add(new ScoreEntry(name, score));
        }
        initGame();
        timer.start();
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    private void addPipe(int x) {
        int pipeHeight = new Random().nextInt(HEIGHT - GROUND_HEIGHT - PIPE_GAP);
        pipes.add(new Rectangle(x, 0, PIPE_WIDTH, pipeHeight));
        pipes.add(new Rectangle(x, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - GROUND_HEIGHT - pipeHeight - PIPE_GAP));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            birdY += birdVelocity;
            birdVelocity += 1;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5 + score / 200;
            }

            if (pipes.get(0).x + PIPE_WIDTH < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe(pipes.get(pipes.size() - 1).x + PIPE_INTERVAL);
                score++;
            }

            checkCollision();
            repaint();
        }
    }

    private void checkCollision() {
        Rectangle birdRect = new Rectangle(birdX, birdY, BIRD_SIZE, BIRD_SIZE);
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(birdRect)) {
                gameOver();
                return;
            }
        }

        if (birdY + BIRD_SIZE > HEIGHT - GROUND_HEIGHT || birdY < 0) {
            gameOver();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score);
        restartGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.ORANGE);
        g.fillRect(birdX, birdY, BIRD_SIZE, BIRD_SIZE);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 25);
        g.drawString("High Score: " + highScore, 10, 50);

        int y = 80;
        g.drawString("Leaderboard:", 10, y);
        for (ScoreEntry entry : leaderboard) {
            y += 25;
            g.drawString(entry.name + ": " + entry.score, 10, y);
        }
    }

    private static class ScoreEntry {
        String name;
        int score;

        ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
