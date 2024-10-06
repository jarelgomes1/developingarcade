package Arcade.FlappyBird;

import javax.swing.*;
import java.awt.*;

public class StartScreenPanel extends JPanel {
    public StartScreenPanel(JFrame frame, JPanel buttonPanel) {
        setBackground(Color.CYAN);
        setLayout(new BorderLayout());

        JLabel startLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
        startLabel.setFont(new Font("Serif", Font.BOLD, 36));
        startLabel.setForeground(Color.ORANGE);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Serif", Font.BOLD, 24));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.ORANGE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new FlappyBirdPanel(frame, buttonPanel), BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            frame.getContentPane().revalidate();
        });

        add(startLabel, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }
}
