package Arcade.WesternChess;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard extends JPanel {
    private final Piece[][] board = new Piece[8][8];
    private final List<Piece> capturedPiecesWhite = new ArrayList<>();
    private final List<Piece> capturedPiecesBlack = new ArrayList<>();
    private final JButton[][] buttons = new JButton[8][8];
    private JLabel turnLabel;
    private JLabel statusLabel;
    private JPanel capturedPanelWhite;
    private JPanel capturedPanelBlack;
    private String currentPlayer = "white";
    private boolean pieceSelected = false;
    private int fromRow, fromCol;

    public ChessBoard(JLabel turnLabel, JLabel statusLabel, JPanel capturedPanelWhite, JPanel capturedPanelBlack) {
        this.turnLabel = turnLabel;
        this.statusLabel = statusLabel;
        this.capturedPanelWhite = capturedPanelWhite;
        this.capturedPanelBlack = capturedPanelBlack;

        setLayout(new GridLayout(8, 8));
        initialize();

        Font chessFont = new Font("Serif", Font.PLAIN, 12);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(chessFont);
                buttons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                buttons[i][j].setVerticalAlignment(SwingConstants.CENTER);
                buttons[i][j].addActionListener(new ButtonListener(i, j));

                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    buttons[i][j].setBackground(Color.DARK_GRAY);
                }

                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorderPainted(false);
                add(buttons[i][j]);
            }
        }

        updateBoard();
    }

    public void initialize() {
        String[] pieceOrder = { "Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook" };

        for (int i = 0; i < 8; i++) {
            board[0][i] = new Piece(pieceOrder[i], "black");
            board[1][i] = new Piece("Pawn", "black");
            board[6][i] = new Piece("Pawn", "white");
            board[7][i] = new Piece(pieceOrder[i], "white");
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        capturedPiecesWhite.clear();
        capturedPiecesBlack.clear();
        currentPlayer = "white";
        turnLabel.setText("Turn: White");
        statusLabel.setText("");
    }

    public void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                } else {
                    buttons[i][j].setText(
                            "<html><center>" + board[i][j].toString().replace(" ", "<br>") + "</center></html>");
                    buttons[i][j].setForeground(board[i][j].getColor().equals("white") ? Color.WHITE : Color.BLACK);
                }
            }
        }

        capturedPanelWhite.removeAll();
        for (Piece p : capturedPiecesWhite) {
            JLabel pieceLabel = new JLabel(p.toString());
            pieceLabel.setForeground(Color.BLACK);
            capturedPanelWhite.add(pieceLabel);
        }

        capturedPanelBlack.removeAll();
        for (Piece p : capturedPiecesBlack) {
            JLabel pieceLabel = new JLabel(p.toString());
            pieceLabel.setForeground(Color.BLACK);
            capturedPanelBlack.add(pieceLabel);
        }

        capturedPanelWhite.revalidate();
        capturedPanelWhite.repaint();
        capturedPanelBlack.revalidate();
        capturedPanelBlack.repaint();
    }

    private boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol];

        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        if (isValidMove(fromRow, fromCol, toRow, toCol, piece)) {
            if (board[toRow][toCol] != null) {
                if (board[toRow][toCol].getColor().equals("white")) {
                    capturedPiecesWhite.add(board[toRow][toCol]);
                } else {
                    capturedPiecesBlack.add(board[toRow][toCol]);
                }
            }

            // Handle pawn promotion
            if (piece.getType().equals("Pawn") && (toRow == 0 || toRow == 7)) {
                String[] options = { "Queen", "Rook", "Bishop", "Knight" };
                String newPiece = (String) JOptionPane.showInputDialog(null, "Choose piece for promotion",
                        "Pawn Promotion",
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (newPiece != null) {
                    board[toRow][toCol] = new Piece(newPiece, piece.getColor());
                }
            } else {
                board[toRow][toCol] = piece;
            }

            board[fromRow][fromCol] = null;

            // Check for check and checkmate
            if (isInCheck(currentPlayer)) {
                statusLabel.setText("Check!");
                if (isCheckmate(currentPlayer)) {
                    statusLabel.setText("Checkmate! " + (currentPlayer.equals("white") ? "Black" : "White") + " wins!");
                }
            } else {
                statusLabel.setText("");
            }

            return true;
        }
        return false;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece piece) {
        // Basic boundary check
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }

        // Check if the destination is occupied by a piece of the same color
        Piece destinationPiece = board[toRow][toCol];
        if (destinationPiece != null && destinationPiece.getColor().equals(piece.getColor())) {
            return false;
        }

        String type = piece.getType();
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        switch (type) {
            case "Pawn":
                if (piece.getColor().equals("white")) {
                    if (fromRow == 6 && toRow == 4 && fromCol == toCol && board[5][fromCol] == null
                            && board[4][fromCol] == null) {
                        return true; // initial double move
                    }
                    if (toRow == fromRow - 1 && fromCol == toCol && board[toRow][toCol] == null) {
                        return true; // single move forward
                    }
                    if (toRow == fromRow - 1 && Math.abs(toCol - fromCol) == 1 && board[toRow][toCol] != null) {
                        return true; // capture
                    }
                } else {
                    if (fromRow == 1 && toRow == 3 && fromCol == toCol && board[2][fromCol] == null
                            && board[3][fromCol] == null) {
                        return true; // initial double move
                    }
                    if (toRow == fromRow + 1 && fromCol == toCol && board[toRow][toCol] == null) {
                        return true; // single move forward
                    }
                    if (toRow == fromRow + 1 && Math.abs(toCol - fromCol) == 1 && board[toRow][toCol] != null) {
                        return true; // capture
                    }
                }
                break;
            case "Rook":
                if (fromRow == toRow || fromCol == toCol) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
            case "Knight":
                if (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2) {
                    return true;
                }
                break;
            case "Bishop":
                if (rowDiff == colDiff) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
            case "Queen":
                if (rowDiff == colDiff || fromRow == toRow || fromCol == toCol) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
            case "King":
                if (rowDiff <= 1 && colDiff <= 1) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while (currentRow != toRow || currentCol != toCol) {
            if (board[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    private boolean isInCheck(String color) {
        int[] kingPosition = findKing(color);
        int kingRow = kingPosition[0];
        int kingCol = kingPosition[1];

        String opponentColor = color.equals("white") ? "black" : "white";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    if (isValidMove(i, j, kingRow, kingCol, piece)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getColor().equals(color)) {
                    for (int row = 0; row < 8; row++) {
                        for (int col = 0; col < 8; col++) {
                            Piece originalPiece = board[row][col];
                            if (movePiece(i, j, row, col)) {
                                boolean stillInCheck = isInCheck(color);
                                undoMove(i, j, row, col, originalPiece);
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private int[] findKing(String color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getColor().equals(color) && piece.getType().equals("King")) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    private void undoMove(int fromRow, int fromCol, int toRow, int toCol, Piece originalPiece) {
        board[fromRow][fromCol] = board[toRow][toCol];
        board[toRow][toCol] = originalPiece;
    }

    private class ButtonListener implements ActionListener {
        private final int row;
        private final int col;

        public ButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!pieceSelected) {
                fromRow = row;
                fromCol = col;
                pieceSelected = true;
                highlightPossibleMoves(row, col);
            } else {
                boolean validMove = movePiece(fromRow, fromCol, row, col);
                if (validMove) {
                    currentPlayer = currentPlayer.equals("white") ? "black" : "white";
                    turnLabel.setText("Turn: " + (currentPlayer.equals("white") ? "White" : "Black"));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid move. Try again.");
                }
                pieceSelected = false;
                updateBoard();
            }
        }

        private void highlightPossibleMoves(int row, int col) {
            Piece piece = board[row][col];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isValidMove(row, col, i, j, piece)) {
                        buttons[i][j].setBackground(Color.GREEN);
                    } else {
                        buttons[i][j].setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                    }
                }
            }
        }
    }
}
