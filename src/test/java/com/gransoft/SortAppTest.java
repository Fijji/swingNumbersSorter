package com.gransoft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SortAppTest {
    private SortApp sortApp;

    @BeforeEach
    public void setUp() {
        sortApp = new SortApp();
    }

    @Test
    public void testGenerateRandomNumbers() {
        sortApp.generateRandomNumbers(10);
        List<Integer> numbers = sortApp.numbers;

        assertEquals(10, numbers.size());
        assertTrue(numbers.stream().anyMatch(n -> n <= 30));
        assertTrue(numbers.stream().allMatch(n -> n >= 1 && n <= 1000));
    }

    @Test
    public void testPartition() {
        List<Integer> list = Arrays.asList(10, 80, 30, 90, 40, 50, 70);
        int pivotIndex = sortApp.partition(list, 0, list.size() - 1);

        assertEquals(4, pivotIndex);
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(list.get(i) <= list.get(pivotIndex));
        }
        for (int i = pivotIndex + 1; i < list.size(); i++) {
            assertTrue(list.get(i) > list.get(pivotIndex));
        }
    }


    @Test
    public void testGenerateRandomNumbersAtLeastOneLessThan30() {
        sortApp.generateRandomNumbers(100);
        List<Integer> numbers = sortApp.numbers;

        assertNotNull(numbers);
        assertFalse(numbers.isEmpty());

        assertTrue(numbers.stream().anyMatch(n -> n <= 30));
    }

    @Test
    public void testRandomNumberRange() {
        Random rand = new Random();
        List<Integer> numbers = rand.ints(100, 1, 1001).boxed().collect(Collectors.toList());

        assertTrue(numbers.stream().allMatch(n -> n >= 1 && n <= 1000));
    }
}
