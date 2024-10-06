package Arcade.ThreePlayerChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    private static final int SIZE = 16; // Board size
    private static final int TILE_SIZE = 40; // Size of each tile
    private Piece[][] pieces;
    private Point selectedPiece;
    private int currentPlayer; // 0 for Player 1, 1 for Player 2, 2 for Player 3
    private int capturedPieces;
    private List<Point> possibleMoves;

    public Board() {
        this.pieces = new Piece[SIZE][SIZE];
        this.selectedPiece = null;
        this.currentPlayer = 0;
        this.capturedPieces = 0;
        this.possibleMoves = new ArrayList<>();

        setupPieces();
        setupMouseListeners();
        setupKeyListeners();

        setPreferredSize(new Dimension(SIZE * TILE_SIZE, SIZE * TILE_SIZE));
        setFocusable(true);
    }

    private void setupPieces() {
        // Initialize pieces for three players (player 1: bottom-left, player 2:
        // bottom-right, player 3: top)
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                pieces[row][col] = null; // Clear the board
                if ((row < 4 && col < 4) || (row < 4 && col >= SIZE - 4) || (row >= SIZE - 4 && col >= SIZE - 4)) {
                    pieces[row][col] = new Piece(currentPlayer); // Assign pieces to players
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
            if (pieces[row][col] != null && pieces[row][col].getPlayer() == currentPlayer) {
                selectedPiece = new Point(row, col);
                highlightPossibleMoves(row, col);
            }
        } else {
            Move move = new Move(selectedPiece.x, selectedPiece.y, row, col);
            if (isValidMove(move)) {
                movePiece(move);
                currentPlayer = (currentPlayer + 1) % 3;
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
                        currentPlayer = (currentPlayer + 1) % 3;
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
        // Add logic to highlight possible moves
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

        // Add logic for valid moves, including captures and basic moves

        return true; // Placeholder: Replace with actual move logic
    }

    private void movePiece(Move move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;

        // Add logic for capturing pieces if necessary

        // Update captured pieces
        capturedPieces++;
        ThreePlayerChessGame.updateCapturedPieces(capturedPieces);
    }

    private void checkWinCondition() {
        // Add logic to check win condition for each player

        // Example:
        int player1Pieces = 0;
        int player2Pieces = 0;
        int player3Pieces = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (pieces[row][col] != null) {
                    if (pieces[row][col].getPlayer() == 0) {
                        player1Pieces++;
                    } else if (pieces[row][col].getPlayer() == 1) {
                        player2Pieces++;
                    } else if (pieces[row][col].getPlayer() == 2) {
                        player3Pieces++;
                    }
                }
            }
        }

        if (player1Pieces == 0) {
            ThreePlayerChessGame.showWinPrompt("Player 2 or Player 3");
        } else if (player2Pieces == 0) {
            ThreePlayerChessGame.showWinPrompt("Player 1 or Player 3");
        } else if (player3Pieces == 0) {
            ThreePlayerChessGame.showWinPrompt("Player 1 or Player 2");
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
