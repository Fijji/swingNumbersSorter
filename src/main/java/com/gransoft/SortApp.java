package com.gransoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SortApp {
    private final JFrame frame;
    private final JPanel introPanel;
    private JPanel sortPanel;
    private JPanel buttonPanel;
    private final JTextField inputField;
    List<Integer> numbers;
    private boolean sortDescending = true;
    private boolean hasDisplayedError = false;

    public SortApp() {
        frame = new JFrame("Sort Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        introPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("How many numbers to display?");
        gbc.gridy = 0;
        introPanel.add(label, gbc);

        inputField = new JTextField(5);
        gbc.gridy++;
        introPanel.add(inputField, gbc);

        JButton enterButton = new JButton("Enter");
        gbc.gridy++;
        introPanel.add(enterButton, gbc);

        enterButton.addActionListener(e -> handleNumberInput());

        frame.setContentPane(introPanel);
        frame.setVisible(true);
    }

    private void handleNumberInput() {
        try {
            int numberCount = Integer.parseInt(inputField.getText());
            if (numberCount > 1000) {
                showErrorMessage("Please enter a value less than or equal to 1000.");
                return;
            }
            generateRandomNumbers(numberCount);
            showSortScreen();
        } catch (NumberFormatException ex) {
            showErrorMessage("Please enter a valid number.");
        }
    }

    private void showSortScreen() {
        sortPanel = new JPanel();
        int columns = (numbers.size() + 9) / 10;
        sortPanel.setLayout(new GridLayout(10, columns, 5, 5));

        buttonPanel = createButtonPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(sortPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        updateNumberLabels();
        frame.setContentPane(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setPreferredSize(new Dimension(100, 100));

        JButton sortButton = createButton("Sort", e -> new SortWorker().execute());
        JButton resetButton = createButton("Reset", e -> showIntroScreen());

        panel.add(sortButton);
        panel.add(resetButton);
        return panel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBackground(Color.GREEN);
        button.setOpaque(true);
        button.addActionListener(actionListener);
        return button;
    }

    void generateRandomNumbers(int count) {
        Random rand = new Random();
        numbers = rand.ints(count, 1, 1001).boxed().collect(Collectors.toList());
        if (numbers.stream().noneMatch(n -> n <= 30)) {
            numbers.set(rand.nextInt(count), rand.nextInt(30) + 1);
        }
    }

    private void updateNumberLabels() {
        if (sortPanel == null) {
            if (!hasDisplayedError) {
                showErrorMessage("sortPanel is not initialized.");
                hasDisplayedError = true;
            }
            return;
        }
        hasDisplayedError = false;

        sortPanel.removeAll();
        for (int number : numbers) {
            JLabel label = createNumberLabel(number);
            sortPanel.add(label);
        }
        frame.revalidate();
        frame.repaint();
    }

    private JLabel createNumberLabel(int number) {
        JLabel label = new JLabel(String.valueOf(number), SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.BLUE);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(50, 30));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (number <= 30) {
                    generateRandomNumbers(numbers.size());
                    updateNumberLabels();
                } else {
                    showErrorMessage("Please select a value smaller or equal to 30.");
                }
            }
        });
        return label;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void showIntroScreen() {
        frame.setContentPane(introPanel);
        frame.revalidate();
        frame.repaint();
    }

    void quickSort(List<Integer> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
            updateNumberLabels();
        }
    }

    int partition(List<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j) <= pivot) {
                Collections.swap(list, ++i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    private class SortWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {
            quickSort(numbers, 0, numbers.size() - 1);
            if (!sortDescending) {
                Collections.reverse(numbers);
            }
            return null;
        }

        @Override
        protected void done() {
            sortDescending = !sortDescending;
            updateNumberLabels();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortApp::new);
    }
}
