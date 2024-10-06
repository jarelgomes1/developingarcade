package Arcade.Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisBoard extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int CELL_SIZE = 30;
    private Timer timer;
    private TetrisPiece currentPiece;
    private Point pieceOrigin;
    private Color[][] board;
    private boolean isPaused = false;

    public TetrisBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        timer = new Timer(400, this);
        timer.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentPiece != null && !isPaused) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A: // Left
                            movePiece(-1, 0);
                            break;
                        case KeyEvent.VK_D: // Right
                            movePiece(1, 0);
                            break;
                        case KeyEvent.VK_S: // Down
                            dropPiece();
                            break;
                        case KeyEvent.VK_W: // Rotate
                            rotatePiece();
                            break;
                    }
                }
            }
        });
        setFocusable(true);
        spawnNewPiece();
    }

    private void spawnNewPiece() {
        Point[][] pieceShapes = {
                { new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-2, 0) },
                { new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(1, 0) },
                { new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(-1, 0) },
                { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
                { new Point(0, 0), new Point(0, -1), new Point(-1, 0), new Point(1, 1) },
                { new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(-1, 1) },
                { new Point(0, 0), new Point(0, -1), new Point(-1, 0), new Point(1, -1) }
        };

        Color[] pieceColors = {
                Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
        };

        Random rand = new Random();
        int pieceIndex = rand.nextInt(pieceShapes.length);

        currentPiece = new TetrisPiece(pieceShapes[pieceIndex], pieceColors[pieceIndex]);
        pieceOrigin = new Point(BOARD_WIDTH / 2, 0);
    }

    private boolean isValidMove(Point[] blocks, Point origin) {
        for (Point block : blocks) {
            int x = block.x + origin.x;
            int y = block.y + origin.y;
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (y >= 0 && board[y][x] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean movePiece(int dx, int dy) {
        Point newOrigin = new Point(pieceOrigin.x + dx, pieceOrigin.y + dy);
        if (isValidMove(currentPiece.getBlocks(), newOrigin)) {
            pieceOrigin = newOrigin;
            repaint();
            return true;
        }
        return false;
    }

    private void dropPiece() {
        if (!movePiece(0, 1)) {
            lockPiece();
        }
    }

    private void rotatePiece() {
        currentPiece.rotate();
        if (!isValidMove(currentPiece.getBlocks(), pieceOrigin)) {
            currentPiece.rotateBack();
        } else {
            repaint();
        }
    }

    private void lockPiece() {
        for (Point block : currentPiece.getBlocks()) {
            int x = block.x + pieceOrigin.x;
            int y = block.y + pieceOrigin.y;
            if (y >= 0) {
                board[y][x] = currentPiece.getColor();
            }
        }
        clearLines();
        spawnNewPiece();
    }

    private void clearLines() {
        for (int r = 0; r < BOARD_HEIGHT; r++) {
            boolean fullLine = true;
            for (int c = 0; c < BOARD_WIDTH; c++) {
                if (board[r][c] == null) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                for (int i = r; i > 0; i--) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[i][j] = board[i - 1][j];
                    }
                }
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    board[0][j] = null;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < BOARD_HEIGHT; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                if (board[r][c] != null) {
                    g.setColor(board[r][c]);
                    g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        g.setColor(currentPiece.getColor());
        for (Point block : currentPiece.getBlocks()) {
            int x = block.x + pieceOrigin.x;
            int y = block.y + pieceOrigin.y;
            g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            dropPiece();
            repaint();
        }
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public void restartGame() {
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        spawnNewPiece();
        repaint();
    }
}
