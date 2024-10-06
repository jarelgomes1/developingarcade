package Arcade.CryptoZoo;

public class HybridAnimal extends Animal {
    private int hp;

    public HybridAnimal(String name, String description, String rarity, int value, int hp) {
        super(name, description, rarity, value);
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return super.toString() + ", HP: " + hp;
    }
}
