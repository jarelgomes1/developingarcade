package Arcade.AgarIo;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    private int x, y, size;
    private static final int INITIAL_SIZE = 20;

    public AIPlayer(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = new Random().nextInt(10) + INITIAL_SIZE;
    }

    public void chase(Player player) {
        if (player.getSize() < this.size) {
            moveTowards(player.getX(), player.getY());
        }
    }

    public void chase(List<AIPlayer> aiPlayers, Player player) {
        for (AIPlayer ai : aiPlayers) {
            if (ai != this && ai.getSize() < this.size) {
                moveTowards(ai.getX(), ai.getY());
            }
        }
        chase(player);
    }

    private void moveTowards(int targetX, int targetY) {
        double speed = getSpeed();

        if (targetX > x) {
            x += speed;
        } else if (targetX < x) {
            x -= speed;
        }

        if (targetY > y) {
            y += speed;
        } else if (targetY < y) {
            y -= speed;
        }
    }

    private double getSpeed() {
        return 1.0 / Math.log(size);
    }

    public void update() {
        // AI logic already implemented in chase method
    }

    public void grow(int amount) {
        size += amount;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
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
