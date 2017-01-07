package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EchoCommandTest extends ExecutableTestCase {
    @Test
    public void oneArgumentTest() throws IOException {
        final String arg = "hello";
        final Command echoCommand = Cli.fromTokenized(new String[]{"echo", arg});
        echoCommand.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals(arg, getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void fewArgumentsTest() throws IOException {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> args = new ArrayList<>();
        args.add("echo");
        args.add(arg1);
        args.add(arg2);

        final Command echoCommand = Cli.fromTokenized(args.stream().toArray(String[]::new));
        echoCommand.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals(arg1 + ' ' + arg2, getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

}