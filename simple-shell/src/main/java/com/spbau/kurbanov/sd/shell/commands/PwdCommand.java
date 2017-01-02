package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Command to print name of current/working directory
 */
@Parameters(commandDescription = "Print name of current/working directory")
public class PwdCommand implements Command {

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        out.write(System.getProperty("user.dir").getBytes());
    }
}
