package ru.job4j.states;

public class ParallelSearch {

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        final Thread consumer = new Thread(
                () -> {
                    while (!queue.endOfProducts || !queue.queueIsEmpty()) {
                        try {
                            System.out.println(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        final Thread producer = new Thread(
                () -> {
                    queue.endOfProducts = false;
                    for (int index = 0; index != 3; index++) {
                        try {
                            Thread.sleep(500);
                            queue.offer(index);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.endOfProducts = true;
                }
        );
        producer.start();
        consumer.start();
    }
}
