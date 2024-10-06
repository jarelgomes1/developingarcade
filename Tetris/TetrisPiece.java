package Arcade.Tetris;

import java.awt.Color;
import java.awt.Point;

public class TetrisPiece {
    private Point[] blocks;
    private Color color;
    private int rotationState;

    public TetrisPiece(Point[] blocks, Color color) {
        this.blocks = blocks;
        this.color = color;
        this.rotationState = 0;
    }

    public Point[] getBlocks() {
        return blocks;
    }

    public Color getColor() {
        return color;
    }

    public void rotate() {
        for (Point block : blocks) {
            int temp = block.x;
            block.x = block.y;
            block.y = -temp;
        }
        rotationState = (rotationState + 1) % 4;
    }

    public void rotateBack() {
        for (int i = 0; i < 3; i++) {
            rotate();
        }
    }
}
