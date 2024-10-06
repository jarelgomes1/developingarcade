package Arcade.CryptoZoo;

import java.util.ArrayList;
import java.util.List;

public class Zoo {
    private List<Animal> animals;

    public Zoo() {
        animals = new ArrayList<>();
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public HybridAnimal breedAnimals(Animal parent1, Animal parent2, int additionalHp) {
        String name = parent1.getName() + "-" + parent2.getName();
        String description = "A hybrid of " + parent1.getName() + " and " + parent2.getName();
        int value = (parent1.getValue() + parent2.getValue()) / 2;
        int hp = (parent1 instanceof MonsterVerseAnimal ? ((MonsterVerseAnimal) parent1).getHp() : 0)
                + (parent2 instanceof MonsterVerseAnimal ? ((MonsterVerseAnimal) parent2).getHp() : 0) + additionalHp;
        HybridAnimal hybrid = new HybridAnimal(name, description, "Hybrid", value, hp);
        addAnimal(hybrid);
        return hybrid;
    }
}
