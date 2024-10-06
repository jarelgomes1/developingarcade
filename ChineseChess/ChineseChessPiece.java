package Arcade.ChineseChess;

public class ChineseChessPiece {
    private final String name;
    private final boolean isRed;

    public ChineseChessPiece(String name, boolean isRed) {
        this.name = name;
        this.isRed = isRed;
    }

    public String getName() {
        return name;
    }

    public boolean isRed() {
        return isRed;
    }

    @Override
    public String toString() {
        return (isRed ? "Red " : "Black ") + name;
    }
}
