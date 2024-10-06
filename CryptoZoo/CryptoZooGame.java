package Arcade.CryptoZoo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CryptoZooGame {
    private JFrame frame;
    private Zoo zoo;
    private AnimalDeck deck;
    private JTextArea animalListArea;
    private JLabel balanceLabel;
    private JLabel currentAnimalLabel;
    private JPanel ownedAnimalsPanel;
    private JPanel currentAnimalPanel;
    private int balance;
    private Animal currentAnimal;

    public CryptoZooGame() {
        zoo = new Zoo();
        deck = new AnimalDeck();
        balance = 1000000; // Starting balance of one million dollars
        frame = new JFrame("CryptoZoo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // UI components
        JLabel titleLabel = new JLabel("CryptoZoo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        animalListArea = new JTextArea();
        animalListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(animalListArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        balanceLabel = new JLabel("Balance: $" + balance, SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Serif", Font.BOLD, 16));
        frame.add(balanceLabel, BorderLayout.SOUTH);

        currentAnimalPanel = new JPanel();
        currentAnimalPanel.setLayout(new BoxLayout(currentAnimalPanel, BoxLayout.Y_AXIS));
        currentAnimalLabel = new JLabel("Current Animal: None", SwingConstants.CENTER);
        currentAnimalLabel.setFont(new Font("Serif", Font.BOLD, 16));
        currentAnimalPanel.add(currentAnimalLabel);
        frame.add(currentAnimalPanel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel();
        JButton drawCardButton = new JButton("Draw Animal Card");
        JButton purchaseAnimalButton = new JButton("Purchase Animal");
        JButton breedAnimalsButton = new JButton("Breed Animals");
        JButton battleAnimalButton = new JButton("Battle Animal");
        JButton backButton = new JButton("Back to Menu"); // Added back button

        controlPanel.add(drawCardButton);
        controlPanel.add(purchaseAnimalButton);
        controlPanel.add(breedAnimalsButton);
        controlPanel.add(battleAnimalButton);
        controlPanel.add(backButton); // Added back button to control panel

        frame.add(controlPanel, BorderLayout.NORTH);

        ownedAnimalsPanel = new JPanel();
        ownedAnimalsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JScrollPane ownedAnimalsScrollPane = new JScrollPane(ownedAnimalsPanel);
        ownedAnimalsScrollPane.setPreferredSize(new Dimension(800, 100));
        frame.add(ownedAnimalsScrollPane, BorderLayout.SOUTH);

        // Event listeners
        drawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawAnimalCard();
            }
        });

        purchaseAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseAnimal();
            }
        });

        breedAnimalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breedAnimals();
            }
        });

        battleAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battleAnimal();
            }
        });

        updateAnimalList();
        frame.setVisible(true);
    }

    private void drawAnimalCard() {
        currentAnimal = deck.drawCard();
        if (currentAnimal == null) {
            JOptionPane.showMessageDialog(frame, "No more animals in the deck.");
            return;
        }

        if (currentAnimal instanceof MonsterVerseAnimal) {
            MonsterVerseAnimal monster = (MonsterVerseAnimal) currentAnimal;
            currentAnimalLabel.setText("<html>Current Animal: <br>" + monster.getName() + " <br>(Value: $"
                    + monster.getValue() + ")<br>HP: " + monster.getHp() + "</html>");
        } else {
            currentAnimalLabel.setText("<html>Current Animal: <br>" + currentAnimal.getName() + " <br>(Value: $"
                    + currentAnimal.getValue() + ")</html>");
        }
        currentAnimalLabel.setToolTipText(currentAnimal.toString());
        currentAnimalPanel.setBackground(Color.YELLOW);
    }

    private void purchaseAnimal() {
        if (currentAnimal == null) {
            JOptionPane.showMessageDialog(frame, "You need to draw an animal card first.");
            return;
        }

        int cost = currentAnimal.getValue();
        if (balance < cost) {
            JOptionPane.showMessageDialog(frame, "Not enough balance to purchase this animal.");
            return;
        }

        balance -= cost;
        zoo.addAnimal(currentAnimal);
        currentAnimal = null;
        currentAnimalLabel.setText("Current Animal: None");
        currentAnimalPanel.setBackground(null);
        updateAnimalList();
        updateOwnedAnimals();
        updateBalanceLabel();
    }

    private void breedAnimals() {
        if (zoo.getAnimals().size() < 2) {
            JOptionPane.showMessageDialog(frame, "Not enough animals to breed.");
            return;
        }

        Animal parent1 = (Animal) JOptionPane.showInputDialog(frame, "Select first parent:", "Breed Animals",
                JOptionPane.QUESTION_MESSAGE, null, zoo.getAnimals().toArray(), null);
        Animal parent2 = (Animal) JOptionPane.showInputDialog(frame, "Select second parent:", "Breed Animals",
                JOptionPane.QUESTION_MESSAGE, null, zoo.getAnimals().toArray(), null);

        if (parent1 != null && parent2 != null && parent1 != parent2) {
            int hp;
            if (parent1 instanceof MonsterVerseAnimal && parent2 instanceof MonsterVerseAnimal) {
                hp = (((MonsterVerseAnimal) parent1).getHp() + ((MonsterVerseAnimal) parent2).getHp()) / 2;
            } else if (parent1 instanceof MonsterVerseAnimal || parent2 instanceof MonsterVerseAnimal) {
                if (parent1 instanceof MonsterVerseAnimal) {
                    hp = ((MonsterVerseAnimal) parent1).getHp() + 50;
                } else {
                    hp = ((MonsterVerseAnimal) parent2).getHp() + 50;
                }
            } else {
                hp = 50; // Assuming a base HP for hybrids of two normal animals
            }

            String customName = generateHybridName(parent1.getName(), parent2.getName());
            String description = "A hybrid of " + parent1.getName() + " and " + parent2.getName();
            int value = (parent1.getValue() + parent2.getValue()) / 2;
            HybridAnimal hybrid = new HybridAnimal(customName, description, "Hybrid", value, hp);
            zoo.addAnimal(hybrid);
            JOptionPane.showMessageDialog(frame, "Created new hybrid: " + hybrid.getName());
            updateAnimalList();
            updateOwnedAnimals();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid selection. Please try again.");
        }
    }

    private String generateHybridName(String name1, String name2) {
        String part1 = name1.substring(0, name1.length() / 2);
        String part2 = name2.substring(name2.length() / 2);
        return part1 + part2;
    }

    private void battleAnimal() {
        if (currentAnimal == null || !(currentAnimal instanceof MonsterVerseAnimal)) {
            JOptionPane.showMessageDialog(frame, "You need to draw a MonsterVerse animal first.");
            return;
        }

        List<Animal> battleAnimals = new ArrayList<>();
        for (Animal animal : zoo.getAnimals()) {
            if (animal instanceof MonsterVerseAnimal
                    || (animal instanceof HybridAnimal && ((HybridAnimal) animal).getHp() > 0)) {
                battleAnimals.add(animal);
            }
        }

        if (battleAnimals.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "You need to own a MonsterVerse or Hybrid animal with HP to battle.");
            return;
        }

        MonsterVerseAnimal opponent = (MonsterVerseAnimal) currentAnimal;
        Animal yourAnimal = (Animal) JOptionPane.showInputDialog(frame,
                "Select your animal:", "Battle Animal",
                JOptionPane.QUESTION_MESSAGE, null, battleAnimals.toArray(), null);

        if (yourAnimal != null) {
            String battleResult = performBattle(yourAnimal, opponent);
            JOptionPane.showMessageDialog(frame, battleResult);
            if (getHp(opponent) <= 0) {
                currentAnimal = null;
                currentAnimalLabel.setText("Current Animal: None");
                currentAnimalPanel.setBackground(null);
            }
            updateAnimalList();
            updateBalanceLabel();
        }
    }

    private String performBattle(Animal yourAnimal, MonsterVerseAnimal opponent) {
        int yourAnimalHp = getHp(yourAnimal);
        int opponentHp = opponent.getHp();

        while (yourAnimalHp > 0 && opponentHp > 0) {
            opponentHp -= yourAnimalHp / 10; // Simplified damage calculation
            yourAnimalHp -= opponentHp / 10; // Simplified damage calculation
        }

        if (yourAnimalHp > 0) {
            zoo.addAnimal(opponent);
            updateOwnedAnimals();
            return "You won the battle! You now own " + opponent.getName() + "!";
        } else {
            return "You lost the battle! " + opponent.getName() + " remains undefeated.";
        }
    }

    private int getHp(Animal animal) {
        if (animal instanceof MonsterVerseAnimal) {
            return ((MonsterVerseAnimal) animal).getHp();
        } else if (animal instanceof HybridAnimal) {
            return ((HybridAnimal) animal).getHp();
        }
        return 0;
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + balance);
    }

    private void updateAnimalList() {
        StringBuilder animalList = new StringBuilder("Animals in the zoo:\n");
        for (Animal animal : zoo.getAnimals()) {
            animalList.append(animal.toString()).append("\n");
        }
        animalListArea.setText(animalList.toString());
        updateBalanceLabel();
    }

    private void updateOwnedAnimals() {
        ownedAnimalsPanel.removeAll();
        for (Animal animal : zoo.getAnimals()) {
            JLabel animalLabel = new JLabel(
                    animal instanceof HybridAnimal ? ((HybridAnimal) animal).getName().split(" - ")[0]
                            : animal.getName());
            animalLabel.setToolTipText(animal.toString());
            ownedAnimalsPanel.add(animalLabel);
        }
        ownedAnimalsPanel.revalidate();
        ownedAnimalsPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CryptoZooGame());
    }
}
