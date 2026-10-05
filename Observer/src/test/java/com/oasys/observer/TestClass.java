package com.oasys.observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClass {
    @Test
    public void testAddition() {
        int x = add(1,2);
        assertEquals(3, x, "Test if 1 + 2 = 3");
    }

    private int add(int a, int b) {
        return a + b;
    }


}
