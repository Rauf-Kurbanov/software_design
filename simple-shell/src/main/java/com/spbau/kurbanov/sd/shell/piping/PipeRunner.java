package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.commands.CatCommand;
import com.spbau.kurbanov.sd.shell.commands.Command;
import com.spbau.kurbanov.sd.shell.commands.CommandFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class aggregating and running consequent piped com.spbau.kurbanov.sd.shell.commands
 */
@NoArgsConstructor
public class PipeRunner {

    private final LinkedList<Pipe> pipes = new LinkedList<>();

    public PipeRunner(List<String[]> tokenized) throws IOException {
        for (String[] tt : tokenized) {
            Command command = CommandFactory.createFromArray(tt);
            if (command != null) {
                add(command);
            }
        }
    }

    /**
     * Add new command to be executed last
     * @param c
     * @throws IOException
     */
    public void add(Command c) throws IOException {
        if (c instanceof CatCommand) {
            CatCommand cc = (CatCommand) c;
            if (cc.getFileNames().isEmpty()) {
                return;
            }
        }
        if (pipes.isEmpty()) {
            pipes.add(new Pipe(c));
            return;
        }
        pipes.add(new Pipe(pipes.getLast(), c));
    }

    /**
     * Consequently run all com.spbau.kurbanov.sd.shell.commands passing output of the previous command
     * to the next one as input
     * @throws IOException
     */
    public void run() throws IOException {
        for (Pipe pc : pipes) {
            pc.run();
        }
    }
}
