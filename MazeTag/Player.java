package Arcade.MazeTag;

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
    }

    public void undoMove() {
        x -= dx;
        y -= dy;
        dx = 0;
        dy = 0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
