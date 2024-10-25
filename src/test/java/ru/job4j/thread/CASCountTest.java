package ru.job4j.thread;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CASCountTest {

    @Test
    public void test_initial_value_zero() {
        CASCount casCount = new CASCount();
        assertEquals(0, casCount.get());
    }
    
    @Test
    public void test_increment_by_one() {
        CASCount casCount = new CASCount();
        int initial = casCount.get();
        casCount.increment();
        assertEquals(initial + 1, casCount.get());
    }

    @Test
    public void test_increment_by_three() {
        CASCount casCount = new CASCount();
        int initial = casCount.get();
        casCount.increment();
        casCount.increment();
        casCount.increment();
        assertEquals(initial + 3, casCount.get());
    }
}