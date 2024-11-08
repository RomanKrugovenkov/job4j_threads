package ru.job4j.pool;

import java.util.LinkedList;
import java.util.List;

import ru.job4j.states.SimpleBlockingQueue;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;

    public ThreadPool() {
        int size = Runtime.getRuntime().availableProcessors();
        tasks = new SimpleBlockingQueue<>(size);
        Runnable target = () -> {
            try {
                Runnable task = tasks.poll();
                task.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(target, "Thread_" + i);
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        threads.stream().iterator().forEachRemaining(Thread::interrupt);
    }
}
