package ru.job4j.states;

public class Consumer<T> implements Runnable {

    private final SimpleBlockingQueue<T> simpleBlockingQueue;

    public Consumer(SimpleBlockingQueue<T> simpleBlockingQueue) {
        this.simpleBlockingQueue = simpleBlockingQueue;
    }

    @Override
    public void run() {
        while (!simpleBlockingQueue.endOfProducts || !simpleBlockingQueue.queueIsEmpty()) {
            try {
                var product = simpleBlockingQueue.poll();
                System.out.printf("Объект %s извлечен из очереди\n", product.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumer Interrupted");
            }
        }
    }
}
