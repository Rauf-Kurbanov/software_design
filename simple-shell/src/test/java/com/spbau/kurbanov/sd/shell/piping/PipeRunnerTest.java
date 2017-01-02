package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import com.spbau.kurbanov.sd.shell.commands.CatCommand;
import com.spbau.kurbanov.sd.shell.commands.Command;
import com.spbau.kurbanov.sd.shell.commands.ExecutableTestCase;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PipeRunnerTest extends ExecutableTestCase {
    @Test
    public void executeBothCommand() throws IOException {
        final boolean[] executed = new boolean[2];
        final Command first = (in, out, err) -> executed[0] = true;
        final Command second = (in, out, err) -> executed[1] = true;

        final PipeRunner pipeRunner = new PipeRunner();
        pipeRunner.add(first);
        pipeRunner.add(second);
        pipeRunner.run();

        assertTrue(executed[0]);
        assertTrue(executed[1]);
    }


    @Test
    public void pipeContentCorrect() throws IOException {

        final InputStream stdIn = System.in;
        final OutputStream stdOut = System.out;
        final OutputStream stdErr = System.err;
        try {
            System.setIn(EMPTY_INPUT_STREAM);
            System.setErr(new PrintStream(myErrorStream));
            System.setOut(new PrintStream(myOutputStream));

            final String content = "hello";
            final Command echoCommand = Cli.fromTokenized(new String[]{"echo", content});
            final Command catCommand  = new CatCommand();
            final PipeRunner pipeRunner = new PipeRunner();
            pipeRunner.add(echoCommand);
            pipeRunner.add(catCommand);

            pipeRunner.run();

            assertEquals(content, getOutputString().trim());
        } finally {
            System.setIn(stdIn);
            System.setOut(new PrintStream(stdOut));
            System.setErr(new PrintStream(stdErr));
        }
    }

}