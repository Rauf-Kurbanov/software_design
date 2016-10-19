package commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Parameters(commandDescription = " display a line of text")
public class EchoCommand extends Command {

    @Parameter(description = "Strings to echo")
    private final List<String> strings = new ArrayList<>();

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        final String output = strings.stream().collect(Collectors.joining(" "));
        out.write(output.getBytes());
        out.write(System.lineSeparator().getBytes());
    }
}
