package Arcade.Tag;

import java.awt.*;

public class Player {
    private int x, y, dx, dy;
    private static final int SIZE = 20;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
        this.dy = 0;
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public void setDY(int dy) {
        this.dy = dy;
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
