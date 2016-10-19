package piping;

import commands.Command;

import java.io.*;

public class Pipe {

    private InputStream is = System.in;
    private OutputStream os = System.out;
    private OutputStream err = System.err;
    private final Command command;

    public Pipe(Command command) throws IOException {
        this.command = command;
    }

    public Pipe(Pipe last, Command command) throws IOException {
        this.command = command;
        PipedOutputStream pos = new PipedOutputStream();
        last.os = pos;
        is = new PipedInputStream(pos);
    }

    public void run() throws IOException {
        command.run(is, os, err);
    }
}
