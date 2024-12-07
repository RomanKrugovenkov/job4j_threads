package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolColSumTest {

    @Test
    public void test_empty_matrix() {
        int[][] matrix = new int[0][0];
        assertThrows(IllegalArgumentException.class, () -> RolColSum.sum(matrix));
    }

    @Test
    public void test_2x2_matrix_positive_integers() {
        int[][] matrix = {{1, 2}, {3, 4}};
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertEquals(3, result[0].getRowSum());
        assertEquals(4, result[0].getColSum());
        assertEquals(7, result[1].getRowSum());
        assertEquals(6, result[1].getColSum());
    }

    @Test
    public void test_results_collected_in_order() throws Exception {
        int[][] matrix = {{1, 2}, {3, 4}};
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertEquals(3, result[0].getRowSum());
        assertEquals(7, result[1].getRowSum());
    }

    @Test
    public void test_performance_large_matrix() throws Exception {
        int size = 5000;
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 1;
            }
        }
        long start = System.currentTimeMillis();
        RolColSum.asyncSum(matrix);
        long asyncTime = System.currentTimeMillis() - start;
        System.out.println("asyncTime = " + asyncTime);
        start = System.currentTimeMillis();
        RolColSum.sum(matrix);
        long syncTime = System.currentTimeMillis() - start;
        System.out.println("syncTime = " + syncTime);
        assertTrue(asyncTime < syncTime);
    }
}