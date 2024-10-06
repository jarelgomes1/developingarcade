package Arcade.AgarIo;

import java.awt.*;

public class Player {
    private int x, y, dx, dy, size;
    private static final int INITIAL_SIZE = 20;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = INITIAL_SIZE;
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public void setDY(int dy) {
        this.dy = dy;
    }

    public void update() {
        double speed = getSpeed();
        x += dx * speed;
        y += dy * speed;

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > GamePanel.PANEL_WIDTH - size)
            x = GamePanel.PANEL_WIDTH - size;
        if (y > GamePanel.PANEL_HEIGHT - size)
            y = GamePanel.PANEL_HEIGHT - size;
    }

    private double getSpeed() {
        return 1.0 / Math.log(size);
    }

    public void grow(int amount) {
        size += amount;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
