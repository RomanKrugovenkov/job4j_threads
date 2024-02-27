package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                }
        );
        Thread second = new Thread(
                () -> {
                }
        );
        System.out.printf("Статус потока: %s = %s%s", first.getName(), first.getState(), System.lineSeparator());
        System.out.printf("Статус потока: %s = %s%s", second.getName(), second.getState(), System.lineSeparator());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.printf("Ожидание потоков... %s", System.lineSeparator());
        }
        System.out.printf("Статус потока: %s = %s%s", first.getName(), first.getState(), System.lineSeparator());
        System.out.printf("Статус потока: %s = %s%s", second.getName(), second.getState(), System.lineSeparator());
        System.out.printf("Завершена работа потока: %s%s", Thread.currentThread().getName(), System.lineSeparator());
    }
}
