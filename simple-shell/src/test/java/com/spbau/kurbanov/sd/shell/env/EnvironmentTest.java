package com.spbau.kurbanov.sd.shell.env;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnvironmentTest {
    @Test
    public void unknownVariableReturnsEmptyString() {
        final String value = Environment.INSTANCE.getVariableValue("unknownVariable");
        assertTrue(value.isEmpty());
    }

    @Test
    public void overrideOldValue() {
        Environment.INSTANCE.putVariableValue("testVar", "10");
        assertEquals(Environment.INSTANCE.getVariableValue("testVar"), "10");
        Environment.INSTANCE.putVariableValue("testVar", "20");
        assertEquals(Environment.INSTANCE.getVariableValue("testVar"), "20");
    }
}
