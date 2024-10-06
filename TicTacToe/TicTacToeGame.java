package Arcade.TicTacToe;

import javax.swing.*;

import Arcade.ArcadeMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGame {
    public static void main(String[] args) {
        new TicTacToeGame().startGame();
    }

    public void startGame() {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);

        JLabel statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 16));
        frame.add(statusLabel, BorderLayout.NORTH);

        TicTacToeBoard board = new TicTacToeBoard(statusLabel);
        frame.add(board, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Serif", Font.BOLD, 16));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ArcadeMain(); // Assumes you have a MainMenu class to go back to
            }
        });

        frame.add(backButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
