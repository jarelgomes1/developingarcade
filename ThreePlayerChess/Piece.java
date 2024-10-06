package Arcade.ThreePlayerChess;

import java.awt.*;

public class Piece {
    private int player; // 0 for Player 1, 1 for Player 2, 2 for Player 3

    public Piece(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public void draw(Graphics g, int x, int y) {
        switch (player) {
            case 0:
                g.setColor(Color.RED);
                break;
            case 1:
                g.setColor(Color.BLUE);
                break;
            case 2:
                g.setColor(Color.GREEN);
                break;
        }
        g.fillOval(x + 5, y + 5, 30, 30);
    }
}
