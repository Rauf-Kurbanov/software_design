package com.spbau.kurbanov.sd.shell.commands;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Common interface for shell commands
 */
@FunctionalInterface
public interface Command {

    /**
     * Execute the command
     *
     * @param in  shell input stream
     * @param out shell output stream
     * @param err shell error stream
     */
    void run(@NotNull InputStream in,
             @NotNull OutputStream out,
             @NotNull OutputStream err) throws IOException;
}
