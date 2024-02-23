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
        try {
            System.out.printf("Ожидание потока: %s%s", first.getName(), System.lineSeparator());
            first.join();
            System.out.printf("Ожидание потока: %s%s", second.getName(), System.lineSeparator());
            second.join();
        } catch (InterruptedException e) {
            System.out.println("Поток прерван");
        }
        System.out.printf("Статус потока: %s = %s%s", first.getName(), first.getState(), System.lineSeparator());
        System.out.printf("Статус потока: %s = %s%s", second.getName(), second.getState(), System.lineSeparator());
        System.out.printf("Завершена работа потока: %s%s", Thread.currentThread().getName(), System.lineSeparator());
    }
}
