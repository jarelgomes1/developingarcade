package Arcade.Pacman;

public class Pacman {
    private int x, y;
    private Direction direction;
    private int boardSize;

    public Pacman(int x, int y, int boardSize) {
        this.x = x;
        this.y = y;
        this.direction = Direction.RIGHT;
        this.boardSize = boardSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move(boolean[][] walls) {
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
