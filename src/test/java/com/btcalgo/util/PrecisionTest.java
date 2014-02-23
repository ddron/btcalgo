package com.btcalgo.util;

import org.junit.Test;

import static com.btcalgo.util.Precision.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrecisionTest {

    @Test
    public void testIsGreaterOrEqual() {
        assertTrue(isGreaterOrEqual(3.23, 2.12));
        assertTrue(isGreaterOrEqual(3.23, 3.23));
        assertFalse(isGreaterOrEqual(3.22, 3.23));
        assertFalse(isGreaterOrEqual(-3.23, 3.22));
    }

    @Test
    public void testIsGreater() {
        assertTrue(isGreater(3.23, 4.12));
        assertFalse(isGreater(3.23, 2.23));
        assertFalse(isGreater(3.22, 3.22));
        assertFalse(isGreater(-3.23, 3.22));
    }

    @Test
    public void testAreEqual() {
        assertTrue(areEqual(3.23, 3.23));
        assertFalse(areEqual(3.23, 2.23));
        assertFalse(areEqual(-3.22, 3.22));
    }

    @Test
    public void testAreNotEqual() {
        assertFalse(areNotEqual(3.23, 3.23));
        assertTrue(areNotEqual(3.23, 2.23));
        assertTrue(areNotEqual(-3.22, 3.22));
    }

    @Test
    public void testIsLess() {
        assertFalse(isLess(3.23, 3.23));
        assertFalse(isLess(3.23, 2.23));
        assertTrue(isLess(-3.22, 3.22));
        assertTrue(isLess(3.22, 3.23));
    }

}
