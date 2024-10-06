package Arcade.ThreePlayerChess;

public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public boolean isJump() {
        return Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2;
    }

    public int getJumpedRow() {
        if (isJump()) {
            return (fromRow + toRow) / 2;
        }
        return -1; // Not a valid jumped row
    }

    public int getJumpedCol() {
        if (isJump()) {
            return (fromCol + toCol) / 2;
        }
        return -1; // Not a valid jumped col
    }
}
