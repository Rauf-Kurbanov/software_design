package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WcCommandTest extends ExecutableTestCase {
    @Test
    public void emptyInputTest() throws IOException {
        testInternal("", 0, 0);
    }

    @Test
    public void fewEmptyLinesTest() throws IOException {
        String lines = System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
        testInternal(lines, 3, 0);
    }

    @Test
    public void oneLineOneWordTest() throws IOException {
        String data = "word";
        testInternal(data, 1, 1);
    }

    @Test
    public void oneLineFewWordsTest() throws IOException {
        String data = "hehttps://www.youtube.com/watch?v=yOxeZt217uw&index=10&list=vhttps://www.youtube.com/watch?v=yOxeZt217uw&index=10&list=vllo world";
        testInternal(data, 1, 2);
    }

    @Test
    public void fewLinesWithOneWord() throws IOException {
        String firstLine = "hello";
        String secondLine = "world";
        String input = firstLine + System.lineSeparator() + secondLine;
        testInternal(input, 2, 2);
    }

    @Test
    public void fewLinesFewWords() throws IOException {
        String content = "first line" + System.lineSeparator()
                + "second line" + System.lineSeparator()
                + "last third line";
        testInternal(content, 3, 7);
    }

    private void testInternal(String input, int expectedLines, int expectedWords) throws IOException {
        final Command wcCommand = Cli.fromTokenized(new String[]{"wc"});
        wcCommand.run(new ByteArrayInputStream(input.getBytes()), outputStream, errorStream);

        MyWcCommandResult wcResult = parseFromOutput(getOutputString());
        assertNotNull("Could not parse output as three integers", wcResult);
        assertEquals(expectedLines, wcResult.lines);
        assertEquals(expectedWords, wcResult.words);
        assertEquals(input.length(), wcResult.chars);
    }

    private MyWcCommandResult parseFromOutput(String output) {
        final String[] splitResult = output.trim().split("\\s+");
        if (splitResult.length != 3) {
            return null;
        }

        return new MyWcCommandResult(Integer
                .valueOf(splitResult[0]), Integer.valueOf(splitResult[1]), Integer.valueOf(splitResult[2]));
    }

    private static class MyWcCommandResult {
        final int lines;
        final int words;
        final int chars;

        private MyWcCommandResult(int lines, int words, int chars) {
            this.lines = lines;
            this.words = words;
            this.chars = chars;
        }
    }
}
