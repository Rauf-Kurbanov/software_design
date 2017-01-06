package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.commands.Command;

import java.io.*;

/**
 * Adapter allowing to chain shell commands
 * Allows to pass output of the current command as input for next one
 */
public class Pipe {

    private InputStream is = System.in;
    private OutputStream os = System.out;
    private final OutputStream err = System.err;
    private final Command command;

    /**
     * Create Pipe with singe command. Used as first Pipe in a sequence.
     * @param command only command in Pipe
     */
    public Pipe(Command command) throws IOException {
        this.command = command;
    }

    /**
     * Create Pipe by chaining to previous
     * @param last Pipe to get input from
     * @param command command to execute
     */
    public Pipe(Pipe last, Command command) throws IOException {
        this.command = command;
        final PipedOutputStream pos = new PipedOutputStream();
        last.os = pos;
        is = new PipedInputStream(pos);
    }

    public void run() throws IOException {
        command.run(is, os, err);
    }
}
