package Arcade.Solitaire;

public class Card {
    private String suit;
    private String rank;
    private String symbol;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.symbol = calculateSymbol();
    }

    private String calculateSymbol() {
        String suitSymbol;
        switch (suit) {
            case "Hearts":
                suitSymbol = "♥";
                break;
            case "Diamonds":
                suitSymbol = "♦";
                break;
            case "Clubs":
                suitSymbol = "♣";
                break;
            case "Spades":
                suitSymbol = "♠";
                break;
            default:
                suitSymbol = "";
        }
        return rank + suitSymbol;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
