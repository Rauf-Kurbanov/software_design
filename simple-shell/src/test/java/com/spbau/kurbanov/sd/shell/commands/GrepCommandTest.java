package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GrepCommandTest extends ExecutableTestCase {
    @Rule
    public TemporaryFolder rule = new TemporaryFolder();

    @Test
    public void emptyTest() throws IOException {
        Cli.fromTokenized(new String[]{"grep", "pattern"})
                .run(EMPTY_INPUT_STREAM, outputStream, errorStream);

        assertTrue(getOutputString().trim().isEmpty());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void casualGrep() throws IOException {
        final String line = "pattern";
        Cli.fromTokenized(new String[]{"grep", "pattern"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertEquals(line, getOutputString().trim());
        assertTrue(getErrorString().isEmpty());
    }

    @Test
    public void notMatchCaseSensitive() throws IOException {
        final String line = "pattern";
        Cli.fromTokenized(new String[]{"grep", "Pattern"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertTrue(getOutputString().trim().isEmpty());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void matchIgnoreCase() throws IOException {
        final String line = "PatterN";
        Cli.fromTokenized(new String[]{"grep", "pattern", "-i"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertEquals(line, getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void wordPart() throws IOException {
        final String line = "pattern";
        Cli.fromTokenized(new String[]{"grep", "pat"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertEquals(line, getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void entireWordRegex() throws IOException {
        final String line = "pattern";
        Cli.fromTokenized(new String[]{"grep", "pat", "-w"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertTrue(getOutputString().trim().isEmpty());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void entireWordRegexFile() throws IOException {
        final Path tempFile = Files.createTempFile("test", "regex");
        try (OutputStream os = Files.newOutputStream(tempFile)) {
            final PrintStream printer = new PrintStream(os);
            Stream.of("first", "plug", "plugin", "java", "C++", "Groovy")
                    .forEach(printer::println);
        }
        final String regex = "plug";
        final String expected = tempFile + ": " + regex + System.lineSeparator();
        Cli.fromTokenized(new String[]{"grep", "-w", regex, tempFile.toAbsolutePath().toString()})
                .run(new ByteArrayInputStream(new byte[0]), outputStream, errorStream);

        assertEquals(expected.trim(), getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void longRegex() throws IOException {
        final String line = "aa bb cc ee dd";
        Cli.fromTokenized(new String[]{"grep", "a*\\s*b+\\s[bed\\s]*"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertEquals(line, getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void afterContextNotMatch() throws IOException {
        final String line = "first line" + System.lineSeparator() + " second line";
        Cli.fromTokenized(new String[]{"grep", "zzz", "-A", "1"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertTrue(getOutputString().trim().isEmpty());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void afterContextMatch() throws IOException {
        final String line = "first line" + System.lineSeparator() + "second line" + System.lineSeparator() + "third line";
        Cli.fromTokenized(new String[]{"grep", "first", "-A", "1"})
                .run(new ByteArrayInputStream(line.getBytes()), outputStream, errorStream);

        assertEquals("first line" + System.lineSeparator() + "second line", getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());
    }

    @Test
    public void withFile() throws IOException {
        final Path tempFile = Files.createTempFile("test", "regex");
        final String tempPath = tempFile.toAbsolutePath().toString();
        try (OutputStream os = Files.newOutputStream(tempFile)) {
            final PrintStream printer = new PrintStream(os);
            Stream.of("mysql", "python", "ruby", "java", "C++", "Groovy")
                    .forEach(printer::println);
        }
        Cli.fromTokenized(new String[]{"grep", "(python|groovy)", tempPath, "-i", "-A", "1"})
                .run(EMPTY_INPUT_STREAM, outputStream, errorStream);

        final StringJoiner sj = new StringJoiner(System.lineSeparator());
        Stream.of("python", "ruby", "Groovy").map(s -> tempFile + ": " + s + System.lineSeparator()).map(String::trim)
                .forEach(sj::add);
        assertEquals(sj.toString(), getOutputString().trim());
        assertTrue(getErrorString().trim().isEmpty());

        Files.delete(tempFile);
    }
}
