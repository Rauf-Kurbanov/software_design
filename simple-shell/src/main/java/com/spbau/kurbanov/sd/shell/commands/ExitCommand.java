package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Command to cause legit process termination
 */
@Parameters(commandDescription = "Cause legit process termination")
public class ExitCommand implements Command {

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        in.close();
    }
}
