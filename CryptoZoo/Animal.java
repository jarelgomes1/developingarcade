package Arcade.CryptoZoo;

public class Animal {
    private static int idCounter = 1;
    private int id;
    private String name;
    private String description;
    private String rarity;
    private int value;
    private String imagePath; // Placeholder for the image path

    public Animal(String name, String description, String rarity, int value) {
        this.id = idCounter++;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.value = value;
        this.imagePath = "placeholder.png"; // Default placeholder
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name + " " + id;
    }

    public String getDescription() {
        return description;
    }

    public String getRarity() {
        return rarity;
    }

    public int getValue() {
        return value;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\nDescription: %s\nRarity: %s\nValue: $%d", getName(), rarity, description, rarity,
                value);
    }
}
