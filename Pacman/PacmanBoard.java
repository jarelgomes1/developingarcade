package Arcade.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PacmanBoard extends JPanel implements ActionListener {
    private final int BOARD_SIZE = 20; // 20x20 grid
    private final int CELL_SIZE = 40; // Size of each cell in pixels
    private Timer timer;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private List<Point> fruits;
    private boolean[][] walls; // True indicates a wall

    public PacmanBoard() {
        setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TAdapter());

        pacman = new Pacman(1, 1, BOARD_SIZE);
        ghosts = new ArrayList<>();
        fruits = new ArrayList<>();
        walls = new boolean[BOARD_SIZE][BOARD_SIZE];
        generateWalls();
        generateGhosts();
        generateFruits();

        timer = new Timer(150, this);
    }

    private void generateWalls() {
        // Create a simple border of walls
        for (int i = 0; i < BOARD_SIZE; i++) {
            walls[0][i] = true;
            walls[BOARD_SIZE - 1][i] = true;
            walls[i][0] = true;
            walls[i][BOARD_SIZE - 1] = true;
        }

        // Add a more complex inner wall pattern
        for (int i = 2; i < 18; i++) {
            walls[5][i] = true;
            walls[15][i] = true;
        }

        for (int i = 6; i < 15; i++) {
            walls[i][5] = true;
            walls[i][15] = true;
        }

        // Create openings in the walls for ghosts to roam
        walls[10][5] = false;
        walls[10][15] = false;
        walls[5][10] = false;
        walls[15][10] = false;
    }

    private void generateGhosts() {
        ghosts.add(new Ghost(10, 10, BOARD_SIZE));
        ghosts.add(new Ghost(8, 8, BOARD_SIZE));
    }

    private void generateFruits() {
        fruits.add(new Point(3, 3));
        fruits.add(new Point(17, 17));
        fruits.add(new Point(3, 17));
        fruits.add(new Point(17, 3));
    }

    public void startGame() {
        timer.start();
    }

    public void pauseGame() {
        timer.stop();
    }

    public void resumeGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pacman.move(walls);
        moveGhosts();
        checkFruitCollision();
        checkGhostCollision();
        repaint();
    }

    private void moveGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.move(walls, pacman.getX(), pacman.getY());
        }
    }

    private void checkFruitCollision() {
        Point pacmanPos = new Point(pacman.getX(), pacman.getY());
        fruits.removeIf(fruit -> fruit.equals(pacmanPos));
        if (fruits.isEmpty()) {
            winGame();
        }
    }

    private void checkGhostCollision() {
        Point pacmanPos = new Point(pacman.getX(), pacman.getY());
        for (Ghost ghost : ghosts) {
            if (pacmanPos.equals(new Point(ghost.getX(), ghost.getY()))) {
                loseGame();
            }
        }
    }

    private void winGame() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "You win! All fruits collected.");
        restartGame();
    }

    private void loseGame() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Pacman caught by a ghost.");
        restartGame();
    }

    public void restartGame() {
        pacman = new Pacman(1, 1, BOARD_SIZE);
        ghosts.clear();
        fruits.clear();
        generateGhosts();
        generateFruits();
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawFruits(g);
        drawPacman(g);
        drawGhosts(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLUE);
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (walls[y][x]) {
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawFruits(Graphics g) {
        g.setColor(Color.RED);
        for (Point fruit : fruits) {
            g.fillOval(fruit.x * CELL_SIZE, fruit.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawPacman(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(pacman.getX() * CELL_SIZE, pacman.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawGhosts(Graphics g) {
        g.setColor(Color.GREEN);
        for (Ghost ghost : ghosts) {
            g.fillOval(ghost.getX() * CELL_SIZE, ghost.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_A:
                    pacman.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_D:
                    pacman.setDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_W:
                    pacman.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_S:
                    pacman.setDirection(Direction.DOWN);
                    break;
            }
        }
    }
}
