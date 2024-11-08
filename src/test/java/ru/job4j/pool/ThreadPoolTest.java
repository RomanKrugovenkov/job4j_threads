package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {

    @Test
    public void test_tasks_execution_by_threads() throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        AtomicBoolean taskExecuted = new AtomicBoolean(false);
        Runnable task = () -> taskExecuted.set(true);
        threadPool.work(task);
        Thread.sleep(1);
        assertTrue(taskExecuted.get());
    }
}