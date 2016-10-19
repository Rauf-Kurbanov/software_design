package commands;

import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Parameters(commandDescription = "Print word count for each file")
public class WcCommand extends Command {
    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> lines = reader.lines().collect(Collectors.toList());
        int lineCount = lines.size();
        int wordCount = 0;
        int characterCount = 0;
        for (String line : lines) {
            characterCount += line.length();
            wordCount += line.split("\\s+").length;
        }

        out.write(String.format("%d \t%d \t%d", lineCount, wordCount, characterCount).getBytes());
        out.write(System.lineSeparator().getBytes());
    }
}
