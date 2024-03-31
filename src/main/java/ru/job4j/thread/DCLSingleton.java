package ru.job4j.thread;

public class DCLSingleton {
    private static DCLSingleton instance;

    public static DCLSingleton getInstance() {
        synchronized (DCLSingleton.class) {
            if (instance == null) {
                instance = new DCLSingleton();
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }
}
