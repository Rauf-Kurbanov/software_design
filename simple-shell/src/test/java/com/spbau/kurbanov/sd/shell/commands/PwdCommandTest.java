package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PwdCommandTest extends ExecutableTestCase{
    @Test
    public void pwdTest() throws IOException {
        final String currentDirectory = Environment.INSTANCE.getCurrentDirectory().toAbsolutePath().toString();
        new PwdCommand().run(EMPTY_INPUT_STREAM, myOutputStream, myErrorStream);
        assertEquals(currentDirectory, getOutputString().trim());
    }
}
