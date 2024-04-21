package ru.job4j.states;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final int sizeOfQueue;
    boolean endOfProducts = true;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int sizeOfQueue) {
        this.sizeOfQueue = sizeOfQueue;
    }

    public boolean queueIsEmpty() {
        synchronized (this) {
            return queue.isEmpty();
        }
    }
    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == sizeOfQueue) {
                System.out.println("Очередь заполнена");
                wait();
            }
            queue.add(value);
            notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                System.out.println("Очередь пуста");
                wait();
            }
            var rsl = queue.poll();
            notifyAll();
            return rsl;
        }
    }
}
