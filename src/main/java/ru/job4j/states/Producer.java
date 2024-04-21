package ru.job4j.states;

import java.util.List;

public class Producer<T> implements Runnable {

    private final SimpleBlockingQueue<T> simpleBlockingQueue;
    private final List<T> listOfProducts;

    public Producer(SimpleBlockingQueue<T> simpleBlockingQueue, List<T> listOfProducts) {
        this.simpleBlockingQueue = simpleBlockingQueue;
        this.listOfProducts = listOfProducts;
    }

    @Override
    public void run() {
        simpleBlockingQueue.endOfProducts = false;
        for (T object : listOfProducts) {
            try {
                simpleBlockingQueue.offer(object);
                System.out.printf("Объект %s добавлен в очередь\n", object.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer Interrupted");
            }
        }
        simpleBlockingQueue.endOfProducts = true;
    }
}
