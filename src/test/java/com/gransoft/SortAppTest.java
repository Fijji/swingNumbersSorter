package com.gransoft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

public class SortAppTest {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 1000;
    private static final int THRESHOLD = 30;
    private static final int NUMBER_OF_RANDOMS = 10;
    private static final int PARTITION_PIVOT_INDEX = 4;
    private SortApp sortApp;

    @BeforeEach
    public void setUp() throws Exception {
        sortApp = new SortApp();
        setPrivateField(sortApp, "sortPanel", new JPanel());
        setPrivateField(sortApp, "sortButton", new JButton("Sort"));
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    public void testGenerateRandomNumbers() {
        sortApp.generateRandomNumbers(NUMBER_OF_RANDOMS);
        List<Integer> numbers = sortApp.getNumbers();

        assertEquals(NUMBER_OF_RANDOMS, numbers.size());
        assertTrue(numbers.stream().anyMatch(n -> n <= sortApp.getThresholdValue()));
        assertTrue(numbers.stream().allMatch(n -> n >= sortApp.getMinValue() && n <= sortApp.getMaxValue()));
    }

    @Test
    public void testPartition() {
        List<Integer> list = Arrays.asList(10, 80, 30, 90, 40, 50, 70);
        int pivotIndex = sortApp.partition(list, 0, list.size() - 1);

        assertEquals(PARTITION_PIVOT_INDEX, pivotIndex);
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(list.get(i) <= list.get(pivotIndex));
        }
        for (int i = pivotIndex + 1; i < list.size(); i++) {
            assertTrue(list.get(i) > list.get(pivotIndex));
        }
    }

    @Test
    public void testGenerateRandomNumbersAtLeastOneLessThanThreshold() {
        sortApp.generateRandomNumbers(100);
        List<Integer> numbers = sortApp.getNumbers();

        assertNotNull(numbers);
        assertFalse(numbers.isEmpty());
        assertTrue(numbers.stream().anyMatch(n -> n <= THRESHOLD));
    }

    @Test
    public void testRandomNumberRange() {
        Random rand = new Random();
        List<Integer> numbers = rand.ints(100, MIN_NUMBER, MAX_NUMBER + 1).boxed().collect(Collectors.toList());

        assertTrue(numbers.stream().allMatch(n -> n >= MIN_NUMBER && n <= MAX_NUMBER));
    }

    @Test
    public void testQuickSort() {
        List<Integer> list = Arrays.asList(10, 3, 15, 7, 8, 23, 74, 18);
        sortApp.quickSort(list, 0, list.size() - 1);

        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @Test
    public void testReverseSort() {
        sortApp.generateRandomNumbers(NUMBER_OF_RANDOMS);
        List<Integer> numbersBeforeSort = sortApp.getNumbers();

        sortApp.quickSort(numbersBeforeSort, 0, numbersBeforeSort.size() - 1);
        Collections.reverse(numbersBeforeSort);

        for (int i = 0; i < numbersBeforeSort.size() - 1; i++) {
            assertTrue(numbersBeforeSort.get(i) >= numbersBeforeSort.get(i + 1));
        }
    }

    @Test
    public void testSortButton() throws Exception {
        sortApp.generateRandomNumbers(10);

        assertEquals("Sort", sortApp.getSortButton().getText());
    }

    @Test
    public void testRandomNumberGenerationRange() {
        sortApp.generateRandomNumbers(50);
        List<Integer> numbers = sortApp.getNumbers();

        assertTrue(numbers.stream().allMatch(n -> n >= MIN_NUMBER && n <= MAX_NUMBER));
    }
}
