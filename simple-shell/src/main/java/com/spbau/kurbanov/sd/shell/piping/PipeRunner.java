package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.cli.Cli;
import com.spbau.kurbanov.sd.shell.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class aggregating and running consequent piped commands
 */
public class PipeRunner implements Command {
    private final Command leftCommand;
    private final Command rightCommand;

    private PipeRunner(@NotNull Command left, @NotNull Command right) {
        leftCommand = left;
        rightCommand = right;
    }

    public static Command parse(List<String[]> tokenized) throws IOException {
        Command pipeHandler = Cli.fromTokenized(tokenized.get(0));
        for (int i = 1; i < tokenized.size(); i++) {
            pipeHandler = new PipeRunner(pipeHandler, Cli.fromTokenized(tokenized.get(i)));
        }
        return pipeHandler;
    }

    /**
     * Run all aggregated commands sequentially
     */
    public void run(@NotNull InputStream in, @NotNull OutputStream out, @NotNull OutputStream err) throws IOException {
        Pipe pipe = new Pipe();

        leftCommand.run(in, pipe.out, err);
        pipe.out.close();

        rightCommand.run(pipe.in, out, err);
    }

    private static class Pipe {
        private static final int INITIAL_BUFFER_SIZE = 4096;
        private int myOffset = 0;
        private final List<Integer> myBuffer = new ArrayList<>(INITIAL_BUFFER_SIZE);

        private final InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return myOffset < myBuffer.size() ? myBuffer.get(myOffset++) : -1;
            }
        };
        private final OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                myBuffer.add(b);
            }
        };

    }
}