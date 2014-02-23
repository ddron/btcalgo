package com.btcalgo.util;

import org.junit.Test;

import static com.btcalgo.util.StringUtils.isNumber;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void testIsNumber() {
        assertTrue(isNumber("12"));
        assertTrue(isNumber("12.5"));
        assertTrue(isNumber("-12.5"));

        assertFalse(isNumber("abc"));
        assertFalse(isNumber("1,23"));
        assertFalse(isNumber("1.34a"));
    }
}
