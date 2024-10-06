package Arcade.Checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    private static final int SIZE = 8;
    private static final int TILE_SIZE = 100;
    private Piece[][] pieces;
    private Point selectedPiece;
    private boolean currentPlayer; // true for white, false for black
    private int capturedWhitePieces;
    private int capturedBlackPieces;
    private List<Point> possibleMoves;

    public Board() {
        this.pieces = new Piece[SIZE][SIZE];
        this.selectedPiece = null;
        this.currentPlayer = true;
        this.capturedWhitePieces = 0;
        this.capturedBlackPieces = 0;
        this.possibleMoves = new ArrayList<>();

        setupPieces();
        setupMouseListeners();
        setupKeyListeners();

        setPreferredSize(new Dimension(SIZE * TILE_SIZE, SIZE * TILE_SIZE));
        setFocusable(true);
    }

    private void setupPieces() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    if (row < 3) {
                        pieces[row][col] = new Piece(false); // black piece
                    } else if (row > 4) {
                        pieces[row][col] = new Piece(true); // white piece
                    }
                }
            }
        }
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / TILE_SIZE;
                int col = e.getX() / TILE_SIZE;
                handleMouseClick(row, col);
            }
        });
    }

    private void setupKeyListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    private void handleMouseClick(int row, int col) {
        if (selectedPiece == null) {
            if (pieces[row][col] != null && pieces[row][col].isWhite() == currentPlayer) {
                selectedPiece = new Point(row, col);
                highlightPossibleMoves(row, col);
            }
        } else {
            Move move = new Move(selectedPiece.x, selectedPiece.y, row, col);
            if (isValidMove(move)) {
                movePiece(move);
                currentPlayer = !currentPlayer;
                clearPossibleMoves();
                checkWinCondition();
            }
            selectedPiece = null;
        }
        repaint();
    }

    private void handleKeyPress(KeyEvent e) {
        if (selectedPiece == null)
            return;

        int row = selectedPiece.x;
        int col = selectedPiece.y;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                row -= 1;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                row += 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                col -= 1;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                col += 1;
                break;
            case KeyEvent.VK_SPACE:
                if (possibleMoves.contains(new Point(row, col))) {
                    Move move = new Move(selectedPiece.x, selectedPiece.y, row, col);
                    if (isValidMove(move)) {
                        movePiece(move);
                        currentPlayer = !currentPlayer;
                        clearPossibleMoves();
                        checkWinCondition();
                    }
                    selectedPiece = null;
                }
                return;
        }
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            selectedPiece.setLocation(row, col);
            highlightPossibleMoves(selectedPiece.x, selectedPiece.y);
        }

        repaint();
    }

    private void highlightPossibleMoves(int row, int col) {
        possibleMoves.clear();
        for (int dRow = -2; dRow <= 2; dRow++) {
            for (int dCol = -2; dCol <= 2; dCol++) {
                if (isValidMove(new Move(row, col, row + dRow, col + dCol))) {
                    possibleMoves.add(new Point(row + dRow, col + dCol));
                }
            }
        }
    }

    private void clearPossibleMoves() {
        possibleMoves.clear();
    }

    private boolean isValidMove(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        if (toRow < 0 || toRow >= SIZE || toCol < 0 || toCol >= SIZE || pieces[toRow][toCol] != null) {
            return false;
        }

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        if (rowDiff == 1 && colDiff == 1) {
            return true;
        } else if (rowDiff == 2 && colDiff == 2) {
            int middleRow = move.getJumpedRow();
            int middleCol = move.getJumpedCol();
            if (pieces[middleRow][middleCol] != null && pieces[middleRow][middleCol].isWhite() != currentPlayer) {
                return true;
            }
        }

        return false;
    }

    private void movePiece(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;

        if (move.isJump()) {
            int middleRow = move.getJumpedRow();
            int middleCol = move.getJumpedCol();
            pieces[middleRow][middleCol] = null;
            if (currentPlayer) {
                capturedBlackPieces++;
            } else {
                capturedWhitePieces++;
            }
            CheckersGame.updateCapturedPieces(capturedWhitePieces, capturedBlackPieces);
        }
    }

    private void checkWinCondition() {
        int whitePieces = 0;
        int blackPieces = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (pieces[row][col] != null) {
                    if (pieces[row][col].isWhite()) {
                        whitePieces++;
                    } else {
                        blackPieces++;
                    }
                }
            }
        }

        if (whitePieces == 0) {
            CheckersGame.showWinPrompt("Black");
        } else if (blackPieces == 0) {
            CheckersGame.showWinPrompt("White");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                if (pieces[row][col] != null) {
                    pieces[row][col].draw(g, col * TILE_SIZE, row * TILE_SIZE);
                }
            }
        }

        if (selectedPiece != null) {
            g.setColor(Color.RED);
            g.drawRect(selectedPiece.y * TILE_SIZE, selectedPiece.x * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.GREEN);
        for (Point p : possibleMoves) {
            g.drawRect(p.y * TILE_SIZE, p.x * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
}
