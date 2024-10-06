package Arcade.LofiLounge;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import Arcade.ArcadeMain;

public class LofiLounge {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lofi Lounge");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 650);
            frame.setLayout(new BorderLayout());

            JFXPanel jfxPanel = new JFXPanel();
            frame.add(jfxPanel, BorderLayout.CENTER);

            // Define the webView variable outside the Platform.runLater block
            final WebView[] webView = new WebView[1];

            Platform.runLater(() -> {
                webView[0] = new WebView();
                webView[0].getEngine().load("https://www.youtube.com/embed/jfKfPfyJRdk");
                jfxPanel.setScene(new Scene(webView[0]));
            });

            JPanel loungePanel = new LoungePanel();
            frame.add(loungePanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());

            JButton muteButton = new JButton("Mute");
            muteButton.addActionListener(e -> Platform.runLater(
                    () -> webView[0].getEngine().executeScript("document.querySelector('video').muted = true;")));

            JButton unmuteButton = new JButton("Unmute");
            unmuteButton.addActionListener(e -> Platform.runLater(
                    () -> webView[0].getEngine().executeScript("document.querySelector('video').muted = false;")));

            JButton backButton = new JButton("Back to Menu");
            backButton.addActionListener(e -> {
                frame.dispose();
                ArcadeMain.main(null);
            });

            bottomPanel.add(muteButton);
            bottomPanel.add(unmuteButton);
            bottomPanel.add(backButton);

            frame.add(bottomPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
}
