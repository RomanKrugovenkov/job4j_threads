package ru.job4j.pool;

import java.util.concurrent.RecursiveTask;

public class ParallelArraySearch<T> extends RecursiveTask<Integer> {
    private final T value;
    private final T[] array;
    private final int from;
    private final int to;

    public ParallelArraySearch(T value, T[] array, int from, int to) {
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
        for (int i = from; i < to; i++) {
            if (value.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T> Integer findIndexInArray(T value, T[] array) {
        for (int i = 0; i < array.length; i++) {
            if (value.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected Integer compute() {
        if ((to - from) <= 10) {
            return findIndex();
        }
        var middle = (from + to) / 2;
        ParallelArraySearch firstArray = new ParallelArraySearch(value, array, from, middle);
        ParallelArraySearch secondArray = new ParallelArraySearch(value, array, middle + 1, to);
        firstArray.fork();
        secondArray.fork();
        Integer first = (Integer) firstArray.join();
        Integer second = (Integer) secondArray.join();
        return Math.max(first, second);
    }
}