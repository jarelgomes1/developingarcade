package Arcade.LofiLounge;

import javax.swing.*;
import java.awt.*;

public class LoungePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(30, 30, 30));

        // Draw the pixelated lounge scene
        drawLoungeScene(g);
    }

    private void drawLoungeScene(Graphics g) {
        // Draw background
        g.setColor(new Color(50, 50, 50));
        g.fillRect(50, 50, 700, 500);

        // Draw floor
        g.setColor(new Color(60, 60, 60));
        g.fillRect(50, 450, 700, 100);

        // Draw sofa
        g.setColor(new Color(100, 80, 70));
        g.fillRect(100, 350, 200, 80); // Seat
        g.fillRect(90, 300, 220, 50); // Backrest

        // Draw another sofa
        g.setColor(new Color(100, 80, 70));
        g.fillRect(400, 350, 200, 80); // Seat
        g.fillRect(390, 300, 220, 50); // Backrest

        // Draw pixelated characters
        drawPixelatedCharacter(g, 150, 320, Color.PINK); // Character on the left sofa
        drawPixelatedCharacter(g, 450, 320, Color.CYAN); // Character on the right sofa

        // Draw table
        g.setColor(new Color(80, 60, 50));
        g.fillRect(280, 380, 140, 40);

        // Draw drinks on the table
        drawDrink(g, 300, 370, Color.YELLOW);
        drawDrink(g, 360, 370, Color.RED);

        // Draw plants
        drawPlant(g, 60, 400, Color.GREEN);
        drawPlant(g, 680, 400, Color.GREEN);

        // Draw window with view
        g.setColor(new Color(150, 200, 250));
        g.fillRect(250, 70, 300, 180);
        g.setColor(Color.WHITE);
        g.drawRect(250, 70, 300, 180);
        g.drawLine(400, 70, 400, 250);
        g.drawLine(250, 160, 550, 160);

        // Draw the message
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        g.drawString("Enjoy your virtual lounge!", 250, 40);
    }

    private void drawPixelatedCharacter(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x, y, 20, 20); // Head
        g.fillRect(x + 5, y + 20, 10, 20); // Body
        g.fillRect(x - 5, y + 40, 10, 20); // Left leg
        g.fillRect(x + 15, y + 40, 10, 20); // Right leg
        g.fillRect(x - 10, y + 20, 10, 10); // Left arm
        g.fillRect(x + 20, y + 20, 10, 10); // Right arm
    }

    private void drawDrink(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x, y, 10, 20); // Cup
        g.setColor(Color.WHITE);
        g.fillRect(x + 4, y - 5, 2, 5); // Straw
    }

    private void drawPlant(Graphics g, int x, int y, Color color) {
        g.setColor(new Color(50, 30, 20));
        g.fillRect(x + 5, y + 20, 10, 30); // Pot
        g.setColor(color);
        g.fillRect(x, y, 20, 20); // Leaves
    }
}
