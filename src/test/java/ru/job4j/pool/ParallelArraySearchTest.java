package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class ParallelArraySearchTest {

    @Test
    public void test_Throw_when_array_is_empty() {
        Integer[] array = {};
        assertThrows(IllegalArgumentException.class,
                ()->{
            ParallelArraySearch search = new ParallelArraySearch(3, array, 0, array.length);
        }
        );
    }

    @Test
    public void test_find_index_in_small_array() {
        Integer[] array = {1, 2, 3, 4, 5};
        var result = ParallelArraySearch.findIndexInArray(3, array);
        assertEquals(2, result);
    }

    @Test
    public void test_find_index_in_big_array_first() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var result = forkJoinPool.invoke(new ParallelArraySearch(3, array, 0, array.length - 1));
        assertEquals(2, result);
    }

    @Test
    public void test_find_index_in_big_array_second() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var result = forkJoinPool.invoke(new ParallelArraySearch(11, array, 0, array.length - 1));
        assertEquals(10, result);
    }

    @Test
    public void test_notFind_index_in_big_array() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var result = forkJoinPool.invoke(new ParallelArraySearch(13, array, 0, array.length - 1));
        assertEquals(-1, result);
    }
}