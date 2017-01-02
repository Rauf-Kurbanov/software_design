package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Print word count for each file
 */
@Parameters(commandDescription = "Print word count for each file")
public class WcCommand implements Command {
    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        final int[] characterCount = {0};
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new InputStream() {
            @Override
            public int read() throws IOException {
                final int res = in.read();
                if (res != -1) {
                    characterCount[0]++;
                }
                return res;
            }
        }));
        final List<String> lines = reader.lines().collect(Collectors.toList());
        final int lineCount = lines.size();
        int wordCount = 0;
        for (String line : lines) {
            wordCount += Arrays.stream(line.trim().split("\\s+")).filter(x -> !x.isEmpty()).count();
        }

        out.write(String.format("%d \t%d \t%d", lineCount, wordCount, characterCount[0]).getBytes());
        out.write(System.lineSeparator().getBytes());
    }
}
