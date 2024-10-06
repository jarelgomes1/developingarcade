package Arcade.MazeTag;

import java.awt.*;
import java.util.Random;

public class AIPlayer {
    private int x, y, prevX, prevY;
    private static final int SIZE = 20;
    private static final int SPEED = 2;
    private int[][] maze;

    public AIPlayer(int x, int y, int[][] maze) {
        this.x = x;
        this.y = y;
        this.maze = maze;
    }

    public void chase(Player player) {
        prevX = x;
        prevY = y;
        if (player.getX() > x && maze[(x + SIZE) / SIZE][y / SIZE] != 1) {
            x += SPEED;
        } else if (player.getX() < x && maze[(x - SPEED) / SIZE][y / SIZE] != 1) {
            x -= SPEED;
        }

        if (player.getY() > y && maze[x / SIZE][(y + SIZE) / SIZE] != 1) {
            y += SPEED;
        } else if (player.getY() < y && maze[x / SIZE][(y - SPEED) / SIZE] != 1) {
            y -= SPEED;
        }
    }

    public void update() {
        // AI logic already implemented in chase method
    }

    public void undoMove() {
        x = prevX;
        y = prevY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
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
