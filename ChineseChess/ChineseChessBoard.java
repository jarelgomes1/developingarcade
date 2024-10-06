package Arcade.ChineseChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChineseChessBoard extends JPanel {
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_COLS = 9;
    private ChineseChessPiece[][] board;
    private boolean isRedTurn = true;
    private ChineseChessPiece selectedPiece = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int targetRow = -1;
    private int targetCol = -1;
    private List<Point> possibleMoves;
    private List<ChineseChessPiece> redCaptured;
    private List<ChineseChessPiece> blackCaptured;

    private Point dragStart;
    private Point offset;
    private boolean isPaused = false;

    private JPanel capturedRedPanel;
    private JPanel capturedBlackPanel;

    public ChineseChessBoard(JPanel capturedRedPanel, JPanel capturedBlackPanel) {
        this.board = new ChineseChessPiece[BOARD_ROWS][BOARD_COLS];
        this.possibleMoves = new ArrayList<>();
        this.redCaptured = new ArrayList<>();
        this.blackCaptured = new ArrayList<>();
        this.offset = new Point(0, 0);
        this.capturedRedPanel = capturedRedPanel;
        this.capturedBlackPanel = capturedBlackPanel;
        initializeBoard();
        setPreferredSize(new Dimension(BOARD_COLS * getCellSize(), BOARD_ROWS * getCellSize()));
        setBackground(Color.LIGHT_GRAY);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isPaused) {
                    int row = (e.getY() - offset.y) / getCellSize();
                    int col = (e.getX() - offset.x) / getCellSize();
                    handleCellClick(row, col);
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPaused) {
                    dragStart = e.getPoint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isPaused) {
                    Point current = e.getPoint();
                    offset.translate(current.x - dragStart.x, current.y - dragStart.y);
                    dragStart = current;
                    repaint();
                }
            }
        });

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isPaused) {
                    handleKeyPress(e);
                    repaint();
                }
            }
        });
    }

    private int getCellSize() {
        int cellWidth = getWidth() / BOARD_COLS;
        int cellHeight = getHeight() / BOARD_ROWS;
        return Math.min(cellWidth, cellHeight);
    }

    public void initializeBoard() {
        // Clear the board
        for (int r = 0; r < BOARD_ROWS; r++) {
            for (int c = 0; c < BOARD_COLS; c++) {
                board[r][c] = null;
            }
        }

        // Clear captured pieces
        redCaptured.clear();
        blackCaptured.clear();
        capturedRedPanel.removeAll();
        capturedBlackPanel.removeAll();

        // Initialize red pieces
        board[0][0] = new ChineseChessPiece("車", true);
        board[0][8] = new ChineseChessPiece("車", true);
        board[0][1] = new ChineseChessPiece("馬", true);
        board[0][7] = new ChineseChessPiece("馬", true);
        board[0][2] = new ChineseChessPiece("相", true);
        board[0][6] = new ChineseChessPiece("相", true);
        board[0][3] = new ChineseChessPiece("仕", true);
        board[0][5] = new ChineseChessPiece("仕", true);
        board[0][4] = new ChineseChessPiece("帥", true);
        board[2][1] = new ChineseChessPiece("炮", true);
        board[2][7] = new ChineseChessPiece("炮", true);
        board[3][0] = new ChineseChessPiece("兵", true);
        board[3][2] = new ChineseChessPiece("兵", true);
        board[3][4] = new ChineseChessPiece("兵", true);
        board[3][6] = new ChineseChessPiece("兵", true);
        board[3][8] = new ChineseChessPiece("兵", true);

        // Initialize black pieces
        board[9][0] = new ChineseChessPiece("車", false);
        board[9][8] = new ChineseChessPiece("車", false);
        board[9][1] = new ChineseChessPiece("馬", false);
        board[9][7] = new ChineseChessPiece("馬", false);
        board[9][2] = new ChineseChessPiece("象", false);
        board[9][6] = new ChineseChessPiece("象", false);
        board[9][3] = new ChineseChessPiece("士", false);
        board[9][5] = new ChineseChessPiece("士", false);
        board[9][4] = new ChineseChessPiece("將", false);
        board[7][1] = new ChineseChessPiece("砲", false);
        board[7][7] = new ChineseChessPiece("砲", false);
        board[6][0] = new ChineseChessPiece("卒", false);
        board[6][2] = new ChineseChessPiece("卒", false);
        board[6][4] = new ChineseChessPiece("卒", false);
        board[6][6] = new ChineseChessPiece("卒", false);
        board[6][8] = new ChineseChessPiece("卒", false);

        isRedTurn = true;
        selectedPiece = null;
        selectedRow = -1;
        selectedCol = -1;
        targetRow = -1;
        targetCol = -1;
        possibleMoves.clear();
        repaint();
    }

    private void handleCellClick(int row, int col) {
        ChineseChessPiece piece = board[row][col];
        if (selectedPiece == null) {
            if (piece != null && piece.isRed() == isRedTurn) {
                selectedPiece = piece;
                selectedRow = row;
                selectedCol = col;
                targetRow = row;
                targetCol = col;
                possibleMoves = getPossibleMoves(selectedRow, selectedCol);
            }
        } else {
            if (isValidMove(selectedRow, selectedCol, row, col)) {
                ChineseChessPiece capturedPiece = board[row][col];
                if (capturedPiece != null) {
                    if (capturedPiece.isRed()) {
                        redCaptured.add(capturedPiece);
                        JLabel pieceLabel = new JLabel(capturedPiece.getName());
                        pieceLabel.setForeground(Color.RED);
                        capturedBlackPanel.add(pieceLabel);
                    } else {
                        blackCaptured.add(capturedPiece);
                        JLabel pieceLabel = new JLabel(capturedPiece.getName());
                        pieceLabel.setForeground(Color.BLACK);
                        capturedRedPanel.add(pieceLabel);
                    }
                    capturedRedPanel.revalidate();
                    capturedBlackPanel.revalidate();

                    // Check if a general is taken out
                    if (capturedPiece.getName().equals("帥") || capturedPiece.getName().equals("將")) {
                        String winner = isRedTurn ? "Red" : "Black";
                        String message = winner + " wins! Game Over! Do you want to restart or quit?";
                        showEndGameDialog(message);
                        return; // End the method here since the game is over
                    }
                }
                board[row][col] = selectedPiece;
                board[selectedRow][selectedCol] = null;
                selectedPiece = null;
                possibleMoves.clear();
                isRedTurn = !isRedTurn;
                checkForFlyingGeneral();
            } else {
                selectedPiece = null;
                possibleMoves.clear();
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (selectedPiece == null)
            return;

        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W:
                if (targetRow > 0)
                    targetRow--;
                break;
            case KeyEvent.VK_S:
                if (targetRow < BOARD_ROWS - 1)
                    targetRow++;
                break;
            case KeyEvent.VK_A:
                if (targetCol > 0)
                    targetCol--;
                break;
            case KeyEvent.VK_D:
                if (targetCol < BOARD_COLS - 1)
                    targetCol++;
                break;
            case KeyEvent.VK_SPACE:
                handleCellClick(targetRow, targetCol);
                break;
        }
    }

    private List<Point> getPossibleMoves(int row, int col) {
        List<Point> moves = new ArrayList<>();
        for (int r = 0; r < BOARD_ROWS; r++) {
            for (int c = 0; c < BOARD_COLS; c++) {
                if (isValidMove(row, col, r, c)) {
                    moves.add(new Point(r, c));
                }
            }
        }
        return moves;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (board[toRow][toCol] != null && board[toRow][toCol].isRed() == isRedTurn) {
            return false;
        }

        ChineseChessPiece piece = board[fromRow][fromCol];
        switch (piece.getName()) {
            case "車": // Rook
                return isValidRookMove(fromRow, fromCol, toRow, toCol);
            case "馬": // Knight
                return isValidKnightMove(fromRow, fromCol, toRow, toCol);
            case "相": // Elephant (Red)
            case "象": // Elephant (Black)
                return isValidElephantMove(fromRow, fromCol, toRow, toCol, piece.isRed());
            case "仕": // Advisor (Red)
            case "士": // Advisor (Black)
                return isValidAdvisorMove(fromRow, fromCol, toRow, toCol, piece.isRed());
            case "帥": // General (Red)
            case "將": // General (Black)
                return isValidGeneralMove(fromRow, fromCol, toRow, toCol, piece.isRed());
            case "炮": // Cannon (Red)
            case "砲": // Cannon (Black)
                return isValidCannonMove(fromRow, fromCol, toRow, toCol);
            case "兵": // Soldier (Red)
            case "卒": // Soldier (Black)
                return isValidSoldierMove(fromRow, fromCol, toRow, toCol, piece.isRed());
            default:
                return false;
        }
    }

    private boolean isValidRookMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        if (fromRow == toRow) {
            int minCol = Math.min(fromCol, toCol);
            int maxCol = Math.max(fromCol, toCol);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (board[fromRow][col] != null) {
                    return false;
                }
            }
        } else {
            int minRow = Math.min(fromRow, toRow);
            int maxRow = Math.max(fromRow, toRow);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (board[row][fromCol] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidKnightMove(int fromRow, int fromCol, int toRow, int toCol) {
        int dRow = Math.abs(fromRow - toRow);
        int dCol = Math.abs(fromCol - toCol);
        if ((dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2)) {
            int middleRow = fromRow + (toRow - fromRow) / 2;
            int middleCol = fromCol + (toCol - fromCol) / 2;
            if (dRow == 2) {
                return board[middleRow][fromCol] == null;
            } else {
                return board[fromRow][middleCol] == null;
            }
        }
        return false;
    }

    private boolean isValidElephantMove(int fromRow, int fromCol, int toRow, int toCol, boolean isRed) {
        int dRow = Math.abs(fromRow - toRow);
        int dCol = Math.abs(fromCol - toCol);
        if (dRow == 2 && dCol == 2) {
            int middleRow = fromRow + (toRow - fromRow) / 2;
            int middleCol = fromCol + (toCol - fromCol) / 2;
            if (board[middleRow][middleCol] != null) {
                return false;
            }
            if (isRed) {
                return toRow <= 4;
            } else {
                return toRow >= 5;
            }
        }
        return false;
    }

    private boolean isValidAdvisorMove(int fromRow, int fromCol, int toRow, int toCol, boolean isRed) {
        int dRow = Math.abs(fromRow - toRow);
        int dCol = Math.abs(fromCol - toCol);
        if (dRow == 1 && dCol == 1) {
            if (isRed) {
                return toRow >= 7 && toRow <= 9 && toCol >= 3 && toCol <= 5;
            } else {
                return toRow >= 0 && toRow <= 2 && toCol >= 3 && toCol <= 5;
            }
        }
        return false;
    }

    private boolean isValidGeneralMove(int fromRow, int fromCol, int toRow, int toCol, boolean isRed) {
        int dRow = Math.abs(fromRow - toRow);
        int dCol = Math.abs(fromCol - toCol);
        if ((dRow == 1 && dCol == 0) || (dRow == 0 && dCol == 1)) {
            if (isRed) {
                return toRow >= 7 && toRow <= 9 && toCol >= 3 && toCol <= 5;
            } else {
                return toRow >= 0 && toRow <= 2 && toCol >= 3 && toCol <= 5;
            }
        }
        return false;
    }

    private boolean isValidCannonMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        int minRow = Math.min(fromRow, toRow);
        int maxRow = Math.max(fromRow, toRow);
        int minCol = Math.min(fromCol, toCol);
        int maxCol = Math.max(fromCol, toCol);
        int obstacles = 0;

        if (fromRow == toRow) {
            for (int col = minCol + 1; col < maxCol; col++) {
                if (board[fromRow][col] != null) {
                    obstacles++;
                }
            }
        } else {
            for (int row = minRow + 1; row < maxRow; row++) {
                if (board[row][fromCol] != null) {
                    obstacles++;
                }
            }
        }

        if (obstacles == 0 && board[toRow][toCol] == null) {
            return true;
        } else if (obstacles == 1 && board[toRow][toCol] != null) {
            return true;
        }
        return false;
    }

    private boolean isValidSoldierMove(int fromRow, int fromCol, int toRow, int toCol, boolean isRed) {
        int dRow = toRow - fromRow;
        int dCol = Math.abs(fromCol - toCol);
        if (dCol > 1 || dRow * (isRed ? 1 : -1) != 1) {
            return false;
        }
        if (isRed) {
            return toRow >= fromRow;
        } else {
            return toRow <= fromRow;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = getCellSize();

        // Draw the board grid
        for (int row = 0; row <= BOARD_ROWS; row++) {
            int y = row * cellSize + offset.y;
            g.drawLine(offset.x, y, (BOARD_COLS - 1) * cellSize + offset.x, y);
        }
        for (int col = 0; col <= BOARD_COLS; col++) {
            int x = col * cellSize + offset.x;
            g.drawLine(x, offset.y, x, (BOARD_ROWS - 1) * cellSize + offset.y);
        }

        // Draw the river
        int riverY = 5 * cellSize + offset.y;
        g.setColor(new Color(173, 216, 230)); // Light blue color for the river
        g.fillRect(offset.x, riverY - cellSize / 2, (BOARD_COLS - 1) * cellSize, cellSize);

        // Draw the pieces on intersections
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                int x = col * cellSize + offset.x;
                int y = row * cellSize + offset.y;

                ChineseChessPiece piece = board[row][col];
                if (piece != null) {
                    g.setColor(piece.isRed() ? Color.RED : Color.BLACK);
                    g.setFont(new Font("Serif", Font.BOLD, cellSize / 2));
                    g.drawString(piece.getName(), x - cellSize / 4, y + cellSize / 4);
                }
            }
        }

        // Highlight selected piece and target cell
        if (selectedPiece != null) {
            int x = selectedCol * cellSize + offset.x;
            int y = selectedRow * cellSize + offset.y;
            g.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red for selected piece
            g.fillRect(x - cellSize / 2, y - cellSize / 2, cellSize, cellSize);

            int targetX = targetCol * cellSize + offset.x;
            int targetY = targetRow * cellSize + offset.y;
            g.setColor(new Color(0, 255, 0, 128)); // Semi-transparent green for target cell
            g.fillRect(targetX - cellSize / 2, targetY - cellSize / 2, cellSize, cellSize);
        }

        // Draw possible moves
        g.setColor(new Color(0, 255, 0, 128));
        for (Point move : possibleMoves) {
            int x = move.y * cellSize + offset.x;
            int y = move.x * cellSize + offset.y;
            g.fillRect(x - cellSize / 2, y - cellSize / 2, cellSize, cellSize);
        }

        // Draw current player's turn
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        String turnText = isRedTurn ? "Red's Turn" : "Black's Turn";
        g.drawString(turnText, getWidth() / 2 - g.getFontMetrics().stringWidth(turnText) / 2, getHeight() - 10);
    }

    private void checkForFlyingGeneral() {
        boolean redGeneralVisible = false;
        boolean blackGeneralVisible = false;
        int redGeneralRow = -1;
        int blackGeneralRow = -1;
        int generalCol = -1;

        // Find the positions of both generals
        for (int c = 0; c < BOARD_COLS; c++) {
            for (int r = 0; r < BOARD_ROWS; r++) {
                ChineseChessPiece piece = board[r][c];
                if (piece != null) {
                    if (piece.getName().equals("帥") && piece.isRed()) {
                        redGeneralVisible = true;
                        redGeneralRow = r;
                        generalCol = c;
                    }
                    if (piece.getName().equals("將") && !piece.isRed()) {
                        blackGeneralVisible = true;
                        blackGeneralRow = r;
                        generalCol = c;
                    }
                }
            }

            if (redGeneralVisible && blackGeneralVisible && generalCol != -1) {
                // Check if there's no piece between the two generals in the same column
                boolean hasPieceBetween = false;
                for (int r = Math.min(redGeneralRow, blackGeneralRow) + 1; r < Math.max(redGeneralRow,
                        blackGeneralRow); r++) {
                    if (board[r][generalCol] != null) {
                        hasPieceBetween = true;
                        break;
                    }
                }
                if (!hasPieceBetween) {
                    String winner = isRedTurn ? "Red" : "Black";
                    String message = winner
                            + " wins by Flying General Move! Game Over! Do you want to restart or quit?";
                    showEndGameDialog(message);
                }
            }
        }
    }

    public void showEndGameDialog(String message) {
        int response = JOptionPane.showOptionDialog(
                this,
                message,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[] { "Restart", "Quit" },
                "Restart");

        if (response == 0) {
            initializeBoard();
        } else if (response == 1) {
            System.exit(0);
        }
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public JPanel createCapturedPiecesPanel() {
        JPanel capturedPanel = new JPanel(new GridLayout(2, 1));
        capturedRedPanel.setBorder(BorderFactory.createTitledBorder("Captured by Black"));
        capturedBlackPanel.setBorder(BorderFactory.createTitledBorder("Captured by Red"));
        capturedPanel.add(capturedRedPanel);
        capturedPanel.add(capturedBlackPanel);
        return capturedPanel;
    }
}
