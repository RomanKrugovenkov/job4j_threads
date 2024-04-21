package ru.job4j.states;

import org.junit.jupiter.api.Test;

import java.util.List;

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
}