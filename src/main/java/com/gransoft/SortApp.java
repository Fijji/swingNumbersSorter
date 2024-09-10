package com.gransoft;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SortApp {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 1000;
    private static final int MAX_COLUMNS = 10;
    private static final int THRESHOLD_VALUE = 30;
    private static final Random RANDOM = new Random();
    private final JFrame frame;
    private final JPanel introPanel;
    private final JTextField inputField;
    private boolean sortDescending = true;
    private boolean hasDisplayedError = false;
    private List<Integer> numbers;
    private JButton sortButton;
    private JPanel sortPanel;

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
            if (numberCount < MIN_VALUE || numberCount > MAX_VALUE) {
                showErrorMessage("Please enter a value less than or equal to " + MAX_VALUE + ". (Min = " + MIN_VALUE + ")");
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
        int columns = (numbers.size() + MAX_COLUMNS - 1) / MAX_COLUMNS;
        sortPanel.setLayout(new GridLayout(MAX_COLUMNS, columns, 5, 5));

        JPanel buttonPanel = createButtonPanel();

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

        sortButton = createButton("Sort", e -> new SortWorker().execute());
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
        numbers = RANDOM.ints(count, MIN_VALUE, MAX_VALUE + 1).boxed().collect(Collectors.toList());
        if (numbers.stream().noneMatch(n -> n <= THRESHOLD_VALUE)) {
            numbers.set(RANDOM.nextInt(count), RANDOM.nextInt(THRESHOLD_VALUE) + MIN_VALUE);
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
            JLabel label = createNumberLabel(number, false);
            sortPanel.add(label);
        }
        frame.revalidate();
        frame.repaint();
    }

    private JLabel createNumberLabel(int number, boolean isSorted) {
        JLabel label = new JLabel(String.valueOf(number), SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(isSorted ? Color.GRAY : Color.BLUE);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(50, 30));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (number <= THRESHOLD_VALUE) {
                    generateRandomNumbers(number);
                    updateNumberLabels();
                } else {
                    showErrorMessage("Please select a value smaller or equal to " + THRESHOLD_VALUE + ".");
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
            updateNumberLabelsWithSortedState(list, pivotIndex);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private void updateNumberLabelsWithSortedState(List<Integer> list, int pivotIndex) {
        sortPanel.removeAll();
        for (int i = 0; i < list.size(); i++) {
            JLabel label = createNumberLabel(list.get(i), i <= pivotIndex);
            sortPanel.add(label);
        }
        frame.revalidate();
        frame.repaint();
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
            sortButton.setText("Sorting...");
            sortButton.setEnabled(false);
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
            sortButton.setText("<html>Reverse<br>Sort</html>");
            sortButton.setEnabled(true);
        }
    }

    List<Integer> getNumbers() {
        return numbers;
    }

    JButton getSortButton() {
        return sortButton;
    }

    int getThresholdValue() {
        return THRESHOLD_VALUE;
    }

    int getMinValue() {
        return MIN_VALUE;
    }

    int getMaxValue() {
        return MAX_VALUE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortApp::new);
    }
}
