package commands;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Command {

    public abstract  void run(@NotNull InputStream in,
                              @NotNull OutputStream out,
                              @NotNull OutputStream err) throws IOException;
}
