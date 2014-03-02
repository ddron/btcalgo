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
        assertTrue(isGreater(3.23, 2.12));
        assertFalse(isGreater(3.23, 4.23));
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

    @Test
    public void testIzZero() {
        assertTrue(isZero(0.));
        assertFalse(isZero(0.1));
        assertFalse(isZero(- 0.1));
    }

    @Test
    public void testIsAligned() {
        assertTrue(isAlignedAgainst(2, 1));
        assertTrue(isAlignedAgainst(2.1, 0.1));
        assertTrue(isAlignedAgainst(0.0004, 0.0001));
        assertTrue(isAlignedAgainst(2.1234, 0.0002));
        assertTrue(isAlignedAgainst(3.1234, 0.000000002));
        assertTrue(isAlignedAgainst(17.13,  0.00000001));
        assertTrue(isAlignedAgainst(1.32456,  0.00002));

        assertFalse(isAlignedAgainst(3,  2));
        assertFalse(isAlignedAgainst(1.24,  0.1));
    }

}
