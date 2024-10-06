package Arcade.Checkers;

import javax.swing.*;
import java.awt.*;

public class Piece {
    private boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void draw(Graphics g, int x, int y) {
        if (isWhite) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillOval(x + 10, y + 10, 80, 80);
    }
}
