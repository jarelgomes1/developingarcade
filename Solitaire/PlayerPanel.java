package Arcade.Solitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {
    private int playerNumber;
    private List<Card> hand;
    private JPanel cardsPanel;
    private int selectedIndex;

    public PlayerPanel(int playerNumber) {
        this.playerNumber = playerNumber;
        this.hand = new ArrayList<>();
        this.selectedIndex = 0;
        initializePanel();
    }

    private void initializePanel() {
        setBorder(BorderFactory.createTitledBorder("Player " + playerNumber));
        setBackground(new Color(0, 128, 0)); // Green background
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Player " + playerNumber + "'s area");
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.NORTH);

        cardsPanel = new JPanel();
        cardsPanel.setBackground(new Color(0, 128, 0));
        cardsPanel.setLayout(new FlowLayout());
        add(cardsPanel, BorderLayout.CENTER);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    public void addCard(Card card) {
        hand.add(card);
        JLabel cardLabel = new JLabel(card.toString());
        cardLabel.setForeground(Color.WHITE);
        cardLabel.setVisible(false);
        cardLabel.setName(card.toString()); // Use name to identify the card
        cardsPanel.add(cardLabel);
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public void removeCard(Card card) {
        hand.remove(card);
        for (Component component : cardsPanel.getComponents()) {
            if (component instanceof JLabel && component.getName().equals(card.toString())) {
                cardsPanel.remove(component);
                break;
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public int getHandSize() {
        return hand.size();
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public void clearHand() {
        hand.clear();
        cardsPanel.removeAll();
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public void showDeck() {
        for (Component component : cardsPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setVisible(true);
            }
        }
        highlightSelectedCard();
    }

    public void hideDeck() {
        for (Component component : cardsPanel.getComponents()) {
            if (component instanceof JLabel) {
                component.setVisible(false);
            }
        }
    }

    public String getName() {
        return "Player " + playerNumber;
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            selectPreviousCard();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            selectNextCard();
        }
    }

    private void selectPreviousCard() {
        if (selectedIndex > 0) {
            selectedIndex--;
            highlightSelectedCard();
        }
    }

    private void selectNextCard() {
        if (selectedIndex < hand.size() - 1) {
            selectedIndex++;
            highlightSelectedCard();
        }
    }

    private void highlightSelectedCard() {
        for (int i = 0; i < cardsPanel.getComponentCount(); i++) {
            JLabel cardLabel = (JLabel) cardsPanel.getComponent(i);
            if (i == selectedIndex) {
                cardLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            } else {
                cardLabel.setBorder(BorderFactory.createEmptyBorder());
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
