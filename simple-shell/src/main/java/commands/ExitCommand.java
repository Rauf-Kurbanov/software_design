package commands;

import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Parameters(commandDescription = "Cause normal process termination")
public class ExitCommand extends Command {

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        in.close();
    }
}
