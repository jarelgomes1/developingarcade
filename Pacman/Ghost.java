package Arcade.Pacman;

import java.util.Random;

public class Ghost {
    private int x, y;
    private Direction direction;
    private int boardSize;
    private Random random;

    public Ghost(int x, int y, int boardSize) {
        this.x = x;
        this.y = y;
        this.boardSize = boardSize;
        this.random = new Random();
        this.direction = Direction.values()[random.nextInt(Direction.values().length)];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(boolean[][] walls, int pacmanX, int pacmanY) {
        if (random.nextInt(10) < 7) {
            // Move towards Pacman
            if (pacmanX < x) {
                direction = Direction.LEFT;
            } else if (pacmanX > x) {
                direction = Direction.RIGHT;
            } else if (pacmanY < y) {
                direction = Direction.UP;
            } else if (pacmanY > y) {
                direction = Direction.DOWN;
            }
        } else {
            // Random movement
            direction = Direction.values()[random.nextInt(Direction.values().length)];
        }

        int newX = x;
        int newY = y;

        switch (direction) {
            case LEFT:
                newX = (x - 1 + boardSize) % boardSize;
                break;
            case RIGHT:
                newX = (x + 1) % boardSize;
                break;
            case UP:
                newY = (y - 1 + boardSize) % boardSize;
                break;
            case DOWN:
                newY = (y + 1) % boardSize;
                break;
        }

        if (!walls[newY][newX]) {
            x = newX;
            y = newY;
        }
    }
}
