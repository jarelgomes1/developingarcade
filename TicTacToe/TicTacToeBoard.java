package Arcade.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeBoard extends JPanel {
    private JButton[][] buttons = new JButton[3][3];
    private String currentPlayer = "X";
    private JLabel statusLabel;

    public TicTacToeBoard(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        setLayout(new GridLayout(3, 3));
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonListener(i, j));
                add(buttons[i][j]);
            }
        }
    }

    private class ButtonListener implements ActionListener {
        private int row;
        private int col;

        public ButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttons[row][col].getText().equals("") && !isGameWon()) {
                buttons[row][col].setText(currentPlayer);
                if (isGameWon()) {
                    statusLabel.setText("Player " + currentPlayer + " wins!");
                } else if (isBoardFull()) {
                    statusLabel.setText("It's a draw!");
                } else {
                    currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                    statusLabel.setText("Player " + currentPlayer + "'s turn");
                }
            }
        }

        private boolean isGameWon() {
            for (int i = 0; i < 3; i++) {
                if (buttons[i][0].getText().equals(currentPlayer) && buttons[i][1].getText().equals(currentPlayer)
                        && buttons[i][2].getText().equals(currentPlayer)) {
                    return true;
                }
                if (buttons[0][i].getText().equals(currentPlayer) && buttons[1][i].getText().equals(currentPlayer)
                        && buttons[2][i].getText().equals(currentPlayer)) {
                    return true;
                }
            }
            if (buttons[0][0].getText().equals(currentPlayer) && buttons[1][1].getText().equals(currentPlayer)
                    && buttons[2][2].getText().equals(currentPlayer)) {
                return true;
            }
            if (buttons[0][2].getText().equals(currentPlayer) && buttons[1][1].getText().equals(currentPlayer)
                    && buttons[2][0].getText().equals(currentPlayer)) {
                return true;
            }
            return false;
        }

        private boolean isBoardFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
