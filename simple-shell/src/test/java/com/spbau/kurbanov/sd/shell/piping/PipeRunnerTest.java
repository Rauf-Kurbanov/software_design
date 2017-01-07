package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.commands.Command;
import com.spbau.kurbanov.sd.shell.commands.ExecutableTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PipeRunnerTest extends ExecutableTestCase {

    @Test
    public void pipeContentCorrect() throws IOException {

        final Command pipeRunner = PipeRunner.parse(Arrays.asList(new String[]{"echo", "hello"},
                new String[]{"cat"}));
        pipeRunner.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals("hello", getOutputString().trim());
    }

    @Test
    public void pipePipePipe() throws IOException {

        final Command pipeRunner = PipeRunner.parse(Arrays.asList(new String[]{"echo", "hello"},
                new String[]{"cat"}, new String[]{"cat"}, new String[]{"cat"}));
        pipeRunner.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals("hello", getOutputString().trim());
    }

    @Test
    public void pipeExternal() throws IOException {

        final Command pipeRunner = PipeRunner.parse(Arrays.asList(new String[]{"ps"},
                new String[]{"cat"}));
        pipeRunner.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals("PID", getOutputString().trim().substring(0, 3));
    }

}