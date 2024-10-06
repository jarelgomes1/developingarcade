package Arcade.CryptoZoo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class AnimalDeck {
    private List<Animal> deck;
    private List<MonsterVerseAnimal> monsterVerseDeck;
    private static final String[] ANIMAL_NAMES = {
            "Dog", "Cat", "Goat", "Tiger", "Elephant", "Dragon", "Phoenix",
            "Lion", "Bear", "Wolf", "Eagle", "Shark", "Panda", "Kangaroo",
            "Unicorn", "Griffin", "Centaur", "Mermaid", "CyberWolf", "NanoDragon",
            "QuantumPhoenix", "StellarTiger", "HoloBear", "MechaEagle",
            "Horse", "Dolphin", "Rabbit", "Fox", "Deer", "Monkey",
            "Koala", "Squirrel", "Otter", "Raccoon", "Hedgehog", "Penguin",
            "Falcon", "Crocodile", "Alligator", "Chameleon", "Tortoise", "Bat"
    };
    private static final String[] MONSTERVERSE_NAMES = {
            "Godzilla", "King Ghidorah", "MechaGodzilla", "Shin Godzilla", "Burning Godzilla",
            "SpaceGodzilla", "Destoroyah", "Mothra", "Rodan", "Biollante",
            "Gamera", "Gigan", "Hedorah", "Cthulhu", "Kaiju",
            "Kraken", "Leviathan", "Titanosaurus", "Anguirus", "Megalon",
            "Battra", "Orga", "Mecha Kong", "Scar King", "Titan Serpent", "King Kong",
            "MUTO", "MechaGodzilla II", "Kiryu (Mechagodzilla III)", "MechaGhidorah", "Void Ghidorah"
    };

    private static final String[] ANIMAL_DESCRIPTIONS = {
            "A loyal domestic animal.", "A small, furry carnivore.", "A hardy domesticated animal.",
            "A large wild cat with stripes.", "A large mammal with a trunk.", "A mythical fire-breathing creature.",
            "A mythical bird that regenerates from ashes.", "A fierce wild cat.", "A strong and powerful animal.",
            "A cunning and intelligent animal.", "A majestic bird of prey.", "A fearsome ocean predator.",
            "A cute and cuddly bear.", "A hopping marsupial from Australia.",
            "A magical horse with a single horn.",
            "A majestic creature with the body of a lion and the wings of an eagle.",
            "A mythical being with the upper body of a human and the lower body of a horse.",
            "A mythical sea creature with the upper body of a human and the tail of a fish.",
            "A wolf enhanced with cybernetic implants.", "A dragon created with advanced nanotechnology.",
            "A phoenix that rises from quantum particles.", "A tiger that roams the stars.",
            "A bear made of holographic projections.", "An eagle with mechanical wings and body.",
            "A fast and strong domesticated animal.", "A highly intelligent aquatic mammal.",
            "A small, quick mammal known for its long ears.", "A cunning and agile predator.",
            "A graceful and quick mammal with antlers.", "A clever and agile primate.",
            "A small, eucalyptus-loving marsupial.", "A small, bushy-tailed rodent.",
            "A playful aquatic mammal.", "A clever and curious mammal with a mask-like face.",
            "A small, spiky mammal.", "A flightless bird known for its tuxedo-like appearance.",
            "A fast and agile bird of prey.", "A large, reptilian predator.",
            "A large reptile similar to a crocodile.", "A small reptile known for its ability to change color.",
            "A slow-moving reptile with a hard shell.", "A nocturnal flying mammal."
    };
    private static final String[] MONSTERVERSE_DESCRIPTIONS = {
            "The King of the Monsters, a massive dinosaur-like creature.",
            "A three-headed dragon from space.",
            "A giant mechanical monster.",
            "A terrifying, ever-evolving version of Godzilla.",
            "A version of Godzilla supercharged with nuclear energy.",
            "A space clone of Godzilla.",
            "A kaiju created from the Oxygen Destroyer.",
            "A giant moth with divine origins.",
            "A massive pteranodon-like kaiju.",
            "A plant-like kaiju with Godzilla's cells.",
            "A giant flying turtle with jets.",
            "A cyborg kaiju with saw blades.",
            "A kaiju made of pollution.",
            "A cosmic entity of immense power.",
            "A giant monster from Japanese folklore.",
            "A giant sea monster with tentacles.",
            "A colossal underwater creature.",
            "A giant aquatic dinosaur.",
            "A giant ankylosaurus-like kaiju.",
            "A kaiju with drills for hands.",
            "A dark twin of Mothra.",
            "A mutated alien kaiju.",
            "A mechanized version of Kong.",
            "A fearsome alpha predator.",
            "A massive serpentine titan.",
            "A giant ape with immense strength.",
            "Massive Unidentified Terrestrial Organisms.",
            "A more advanced and powerful version of MechaGodzilla.",
            "A bio-mechanical version of Godzilla equipped with advanced weaponry.",
            "A mechanized version of King Ghidorah.",
            "A multi-dimensional version of King Ghidorah."
    };
    private static final int[] MONSTERVERSE_HPS = {
            3200, 3000, 2900, 2800, 2700,
            2600, 2500, 2400, 2300, 2200,
            2100, 2000, 1900, 1800, 1700,
            1600, 1500, 1400, 1300, 1200,
            1100, 1000, 900, 800, 700, 600,
            500, 400, 300, 3500, 3400
    };

    private static final String[] RARITIES = { "Common", "Uncommon", "Rare", "Epic", "Legendary", "Exclusive" };
    private static final int[] VALUES = { 100, 200, 500, 1000, 2000, 10000 };

    private Random random;

    public AnimalDeck() {
        deck = new ArrayList<>();
        monsterVerseDeck = new ArrayList<>();
        initializeDeck();
        initializeMonsterVerseDeck();
        shuffleDeck();
        random = new Random();
    }

    private void initializeDeck() {
        // Add some predefined animals with different rarities and values
        deck.add(new Animal("Dog", "A loyal domestic animal.", "Common", 100));
        deck.add(new Animal("Cat", "A small, furry carnivore.", "Common", 100));
        deck.add(new Animal("Goat", "A hardy domesticated animal.", "Common", 150));
        deck.add(new Animal("Tiger", "A large wild cat with stripes.", "Rare", 500));
        deck.add(new Animal("Elephant", "A large mammal with a trunk.", "Rare", 800));
        deck.add(new Animal("Dragon", "A mythical fire-breathing creature.", "Legendary", 2000));
        deck.add(new Animal("Phoenix", "A mythical bird that regenerates from ashes.", "Legendary", 2500));
        // Add more normal animals
        deck.add(new Animal("Horse", "A fast and strong domesticated animal.", "Common", 200));
        deck.add(new Animal("Dolphin", "A highly intelligent aquatic mammal.", "Rare", 700));
        deck.add(new Animal("Rabbit", "A small, quick mammal known for its long ears.", "Common", 100));
        deck.add(new Animal("Fox", "A cunning and agile predator.", "Uncommon", 300));
        deck.add(new Animal("Deer", "A graceful and quick mammal with antlers.", "Uncommon", 400));
        deck.add(new Animal("Monkey", "A clever and agile primate.", "Rare", 600));
        deck.add(new Animal("Koala", "A small, eucalyptus-loving marsupial.", "Epic", 800));
        deck.add(new Animal("Squirrel", "A small, bushy-tailed rodent.", "Common", 100));
        deck.add(new Animal("Otter", "A playful aquatic mammal.", "Uncommon", 300));
        deck.add(new Animal("Raccoon", "A clever and curious mammal with a mask-like face.", "Uncommon", 350));
        deck.add(new Animal("Hedgehog", "A small, spiky mammal.", "Common", 150));
        deck.add(new Animal("Penguin", "A flightless bird known for its tuxedo-like appearance.", "Rare", 500));
        deck.add(new Animal("Falcon", "A fast and agile bird of prey.", "Epic", 900));
        deck.add(new Animal("Crocodile", "A large, reptilian predator.", "Epic", 1000));
        deck.add(new Animal("Alligator", "A large reptile similar to a crocodile.", "Epic", 950));
        deck.add(new Animal("Chameleon", "A small reptile known for its ability to change color.", "Uncommon", 200));
        deck.add(new Animal("Tortoise", "A slow-moving reptile with a hard shell.", "Uncommon", 300));
        deck.add(new Animal("Bat", "A nocturnal flying mammal.", "Uncommon", 350));
    }

    private void initializeMonsterVerseDeck() {
        for (int i = 0; i < MONSTERVERSE_NAMES.length; i++) {
            monsterVerseDeck.add(new MonsterVerseAnimal(MONSTERVERSE_NAMES[i], MONSTERVERSE_DESCRIPTIONS[i],
                    "Exclusive", 10000, MONSTERVERSE_HPS[i]));
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
        Collections.shuffle(monsterVerseDeck);
    }

    public Animal drawCard() {
        if (random.nextInt(10) < 1 && !monsterVerseDeck.isEmpty()) { // 10% chance to draw a MonsterVerse card
            return drawMonsterVerseCard();
        } else {
            if (deck.isEmpty()) {
                return generateRandomAnimal();
            }
            return deck.remove(0);
        }
    }

    private Animal drawMonsterVerseCard() {
        MonsterVerseAnimal monsterVerseAnimal = monsterVerseDeck.remove(0);
        JOptionPane.showMessageDialog(null,
                "Warning! You have drawn a MonsterVerse creature: " + monsterVerseAnimal.getName() + "!\nHP: "
                        + monsterVerseAnimal.getHp());
        return monsterVerseAnimal;
    }

    private Animal generateRandomAnimal() {
        String name = ANIMAL_NAMES[random.nextInt(ANIMAL_NAMES.length)];
        String description = ANIMAL_DESCRIPTIONS[random.nextInt(ANIMAL_DESCRIPTIONS.length)];
        String rarity = RARITIES[random.nextInt(RARITIES.length - 1)]; // Exclude "Exclusive" for non-MonsterVerse
        int value = VALUES[random.nextInt(VALUES.length - 1)]; // Exclude highest value for non-MonsterVerse
        Animal animal = new Animal(name, description, rarity, value);

        if (rarity.equals("Mythical")) {
            JOptionPane.showMessageDialog(null, "Warning! You have drawn a LEVIATHAN: " + name + "!");
        }

        return animal;
    }
}
