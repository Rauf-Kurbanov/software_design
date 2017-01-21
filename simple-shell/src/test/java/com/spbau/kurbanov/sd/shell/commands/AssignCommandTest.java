package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import com.spbau.kurbanov.sd.shell.env.Environment;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AssignCommandTest extends ExecutableTestCase {
    @Test
    public void isSimpleAssignTest() {
        assertTrue(AssignCommand.isAssignExpression("x=10"));
        assertTrue(AssignCommand.isAssignExpression("x=abc"));
        assertTrue(AssignCommand.isAssignExpression("x=asdasdsad=asdasd"));
        assertFalse(AssignCommand.isAssignExpression("asd123="));
        assertFalse(AssignCommand.isAssignExpression("x="));
        assertFalse(AssignCommand.isAssignExpression("xa sdas="));
        assertFalse(AssignCommand.isAssignExpression("_="));
        assertFalse(AssignCommand.isAssignExpression("123sdf="));
    }

    @Test
    public void simpleVariableSetTest() throws IOException {
        Cli.fromTokenized(new String[]{"x=abc"}).run(EMPTY_INPUT_STREAM, outputStream, errorStream);
        assertEquals("abc", Environment.INSTANCE.getVariableValue("x"));
    }

    @Test
    public void updateExistedValue() throws IOException {
        assertTrue(Environment.INSTANCE.getVariableValue("y").isEmpty());
        Environment.INSTANCE.putVariableValue("y", "hello");
        assertEquals("hello", Environment.INSTANCE.getVariableValue("y"));
        Cli.fromTokenized(new String[]{"y=hi"}).run(EMPTY_INPUT_STREAM, outputStream, errorStream);

        assertEquals(0, outputData.size());
        assertEquals(0, errorData.size());

        assertEquals("hi", Environment.INSTANCE.getVariableValue("y"));
    }
}
