package commands;

import com.beust.jcommander.Parameters;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiredArgsConstructor
@Parameters(commandDescription = "I don't know, some other command")
public class ExternalCommand extends Command {

    @NotNull
    private final String[] tokenized;

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        if (tokenized.length == 0) {
            return;
        }
        ProcessBuilder pb =
                new ProcessBuilder(tokenized);
        final Process start;
        try {
            start = pb.start();
        } catch (IOException cannotRun) {
            out.write(String.format("command not found: %s", tokenized[0]).getBytes());
            return;
        }
        InputStream inputStream = start.getInputStream();
        IOUtils.copy(inputStream, out);
    }
}
