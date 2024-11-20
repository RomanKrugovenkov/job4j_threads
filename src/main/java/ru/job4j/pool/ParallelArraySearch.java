package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelArraySearch extends RecursiveTask<Integer> {
    private final int value;
    private final int[] array;
    private final int from;
    private final int to;

    public ParallelArraySearch(int value, int[] array, int from, int to) {
        if (array.length == 0) {
            System.out.println("Array is empty");
            throw new IllegalArgumentException();
        }
        this.value = value;
        this.array = array;
        this.from = from;
        this.to = to;
    }

    private int findIndex() {
        for (int i = 0; i < array.length; i++) {
            if (value == array[i]) {
                return i + from;
            }
        }
        return -1;
    }

    @Override
    protected Integer compute() {
        if ((to - from) <= 10) {
            return findIndex();
        }
        int middle = (from + to) / 2;
        ParallelArraySearch firstArray = new ParallelArraySearch(value, array, from, middle);
        ParallelArraySearch secondArray = new ParallelArraySearch(value, array, middle + 1, to);
        firstArray.fork();
        secondArray.fork();
        int first = firstArray.join();
        int second = secondArray.join();
        if (first == -1 && second == -1) {
            System.out.println("value not found");
        }
        return first > -1 ? first : second;
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var result = forkJoinPool.invoke(new ParallelArraySearch(7, array, 0, array.length - 1));
        System.out.println(result);
    }
}
