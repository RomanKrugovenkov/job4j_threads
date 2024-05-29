package ru.job4j.states;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {
    @Test
    public void test_add_and_remove_elements() throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(4);
        List<Integer> list = List.of(1, 2, 3, 4);
        Thread producer = new Thread(new Producer<>(simpleBlockingQueue, list));
        Thread consumer = new Thread(new Consumer<>(simpleBlockingQueue));
        producer.start();
        producer.join();
        consumer.start();
        consumer.join();
        assertTrue(simpleBlockingQueue.queueIsEmpty());
    }

    @Test
    public void test_multiple_producers_and_consumers() throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(3);
        List<Integer> list = List.of(1, 2, 3);
        Thread producer1 = new Thread(new Producer<>(simpleBlockingQueue, list));
        Thread producer2 = new Thread(new Producer<>(simpleBlockingQueue, list));
        Thread consumer1 = new Thread(new Consumer<>(simpleBlockingQueue));
        Thread consumer2 = new Thread(new Consumer<>(simpleBlockingQueue));
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();
        assertTrue(simpleBlockingQueue.queueIsEmpty());
    }

    @Test
    public void test_add_and_remove_elements_from_queue_with_one_element() throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(1);
        simpleBlockingQueue.offer(1);
        List<Integer> list = List.of(2);
        Thread producer = new Thread(new Producer<>(simpleBlockingQueue, list));
        Thread consumer = new Thread(new Consumer<>(simpleBlockingQueue));
        producer.start();
        Thread.sleep(500);
        assertEquals(producer.getState(), Thread.State.WAITING);
        consumer.start();
        consumer.join();
        producer.join();
        assertTrue(simpleBlockingQueue.queueIsEmpty());
    }

    @Test
    public void test_consumer_doesnt_start_when_list_is_empty() throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(1);
        List<Integer> list = List.of(1);
        Thread producer = new Thread(new Producer<>(simpleBlockingQueue, list));
        Thread consumer = new Thread(new Consumer<>(simpleBlockingQueue));
        consumer.start();
        Thread.sleep(500);
        assertEquals(consumer.getState(), Thread.State.TERMINATED);
        producer.start();
        producer.join();
        consumer.join();
        assertFalse(simpleBlockingQueue.queueIsEmpty());
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.queueIsEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertTrue(buffer.containsAll(Arrays.asList(0, 1, 2, 3, 4)));
    }
}