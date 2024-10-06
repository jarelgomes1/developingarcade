package Arcade.Tag;

import java.awt.*;
import java.util.Random;

public class AIPlayer {
    private int x, y, dx, dy;
    private static final int SIZE = 20;
    private Random random;

    public AIPlayer(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
        this.random = new Random();
    }

    public void chase(Player player) {
        if (random.nextInt(10) < 5) {
            if (player.getBounds().x > x)
                dx = 1;
            if (player.getBounds().x < x)
                dx = -1;
            if (player.getBounds().y > y)
                dy = 1;
            if (player.getBounds().y < y)
                dy = -1;
        } else {
            dx = 0;
            dy = 0;
        }
    }

    public void update() {
        x += dx;
        y += dy;

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > GamePanel.PANEL_WIDTH - SIZE)
            x = GamePanel.PANEL_WIDTH - SIZE;
        if (y > GamePanel.PANEL_HEIGHT - SIZE)
            y = GamePanel.PANEL_HEIGHT - SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
