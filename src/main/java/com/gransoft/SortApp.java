package com.gransoft;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
    private boolean hasDisplayedError = false;
    private List<Integer> numbers;
    private JButton sortButton;
    private JPanel sortPanel;
    private JPanel numberButtonPanel;
    private JPanel buttonPanel;
    private boolean sortDescending = false;
    private boolean isSortedOnce = false;
    private int highlightIndex1 = -1;
    private int highlightIndex2 = -1;

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
            isSortedOnce = false;
            sortDescending = false;
            showNumberButtonsScreen();
        } catch (NumberFormatException ex) {
            showErrorMessage("Please enter a valid number.");
        }
    }

    private void showNumberButtonsScreen() {
        numberButtonPanel = new JPanel();
        int columns = (numbers.size() + MAX_COLUMNS - 1) / MAX_COLUMNS;
        numberButtonPanel.setLayout(new GridLayout(MAX_COLUMNS, columns, 5, 5));

        buttonPanel = createButtonPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(numberButtonPanel), BorderLayout.CENTER);
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
        JButton resetButton = createButton("Reset", e -> {
            sortDescending = false;
            showIntroScreen();
        });

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
        if (numberButtonPanel == null) {
            if (!hasDisplayedError) {
                showErrorMessage("numberButtonPanel is not initialized.");
                hasDisplayedError = true;
            }
            return;
        }
        hasDisplayedError = false;

        numberButtonPanel.removeAll();
        for (int number : numbers) {
            JButton button = createNumberButton(number);
            numberButtonPanel.add(button);
        }
        frame.revalidate();
        frame.repaint();
    }

    private JButton createNumberButton(int number) {
        JButton button = new JButton(String.valueOf(number));
        button.setOpaque(true);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(50, 30));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (number <= THRESHOLD_VALUE) {
                    generateRandomNumbers(number);
                    updateNumberLabels();
                } else {
                    showErrorMessage("Please select a value smaller or equal to " + THRESHOLD_VALUE + ".");
                }
            }
        });

        return button;
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void showIntroScreen() {
        frame.setContentPane(introPanel);
        frame.revalidate();
        frame.repaint();
    }

    private class SortWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {
            sortButton.setText("Sorting...");
            sortButton.setEnabled(false);

            if (!isSortedOnce) {
                sortPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        visualizeSort(g);
                    }
                };

                frame.getContentPane().removeAll();
                frame.getContentPane().add(new JScrollPane(sortPanel), BorderLayout.CENTER);
                frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
                frame.revalidate();
                frame.repaint();

                quickSort(numbers, 0, numbers.size() - 1);
                isSortedOnce = true;
            } else {
                Collections.reverse(numbers);
            }

            return null;
        }

        @Override
        protected void done() {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new JScrollPane(numberButtonPanel), BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
            updateNumberLabels();

            if (!sortDescending) {
                sortButton.setText("<html>Sort<br>Descending</html>");
            } else {
                sortButton.setText("<html>Sort<br>Ascending</html>");
            }
            sortDescending = !sortDescending;
            sortButton.setEnabled(true);
        }
    }

    void quickSort(List<Integer> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    int partition(List<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j) <= pivot) {
                highlightSwap(i + 1, j);
                Collections.swap(list, ++i, j);
            }
        }
        highlightSwap(i + 1, high);
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    private void visualizeSort(Graphics g) {
        if (numbers == null || numbers.isEmpty()) return;

        int width = sortPanel.getWidth() / numbers.size();
        int maxHeight = sortPanel.getHeight();

        int maxValue = numbers.stream().max(Integer::compareTo).orElse(1);

        for (int i = 0; i < numbers.size(); i++) {
            int height = (int) ((double) numbers.get(i) / maxValue * maxHeight);
            int x = i * width;
            int y = maxHeight - height;

            if (i == highlightIndex1 || i == highlightIndex2) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
            g.fillRect(x, y, width, height);
        }
    }

    private void highlightSwap(int index1, int index2) {
        highlightIndex1 = index1;
        highlightIndex2 = index2;
        sortPanel.repaint();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
