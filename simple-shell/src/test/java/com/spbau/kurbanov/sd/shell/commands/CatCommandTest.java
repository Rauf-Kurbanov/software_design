package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CatCommandTest extends ExecutableTestCase {

    @Rule
    public TemporaryFolder myTemporaryFolder = new TemporaryFolder();

    @Test
    public void inputStreamTest() throws IOException {
        final InputStream is = new ByteArrayInputStream("hello".getBytes());
        new CatCommand().run(is, this.is, errorStream);
        assertEquals("hello", getOutputString());
    }

    @Test
    public void fileTest() throws IOException {
        final String filename = "test";
        final String contentOfFile = "hello!!!";
        final File test = myTemporaryFolder.newFile(filename);
        Files.write(test.toPath(), contentOfFile.getBytes());
        final Command catCommand = Cli.fromTokenized(new String[]{"cat", test.toPath().toString()});
        catCommand.run(EMPTY_INPUT_STREAM, is, errorStream);

        assertEquals(contentOfFile, getOutputString());
        assertTrue(getErrorString().isEmpty());
    }
}