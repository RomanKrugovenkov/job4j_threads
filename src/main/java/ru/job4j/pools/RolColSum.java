package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RolColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }
    }

    private static void checkArray(int[][] matrix) {
        if (matrix.length == 0 || matrix.length != matrix[0].length) {
            System.out.println("Array is incorrect");
            throw new IllegalArgumentException();
        }
    }

    public static Sums[] sum(int[][] matrix) {
        checkArray(matrix);
        Sums[] rsl = new Sums[matrix.length];
        int rowSumTemp = 0;
        int colSumTemp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                rowSumTemp += matrix[i][j];
            }
            for (int n = 0; n < matrix.length; n++) {
                colSumTemp += matrix[n][i];
            }
            rsl[i] = new Sums(rowSumTemp, colSumTemp);
            rowSumTemp = 0;
            colSumTemp = 0;
        }
        return rsl;
    }

    public static Sums[] asyncSum(int[][] matrix) throws Exception {
        checkArray(matrix);
        Sums[] rsl = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        int middle = matrix.length / 2;
        for (int i = 0; i <= middle; i++) {
            futures.put(i, asyncSumI(matrix, i));
            futures.put(matrix.length - 1 - i, asyncSumI(matrix, matrix.length - 1 - i));
        }
        for (Integer key : futures.keySet()) {
            rsl[key] = futures.get(key).get();
        }
        return rsl;
    }

    private static CompletableFuture<Sums> asyncSumI(int[][] matrix, int i) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSumTemp = 0;
            int colSumTemp = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                rowSumTemp += matrix[i][j];
            }
            for (int n = 0; n < matrix.length; n++) {
                colSumTemp += matrix[n][i];
            }
            return new Sums(rowSumTemp, colSumTemp);
        });
    }
}