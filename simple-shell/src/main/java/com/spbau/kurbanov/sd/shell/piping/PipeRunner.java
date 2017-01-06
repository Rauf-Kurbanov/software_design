package com.spbau.kurbanov.sd.shell.piping;

import com.spbau.kurbanov.sd.shell.commands.CatCommand;
import com.spbau.kurbanov.sd.shell.commands.Command;
import com.spbau.kurbanov.sd.shell.commands.CommandFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class aggregating and running consequent piped commands
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
     * @param command new command to add
     */
    public void add(Command command) throws IOException {
        if (command instanceof CatCommand) {
            final CatCommand cc = (CatCommand) command;
            if (cc.getFileNames().isEmpty()) {
                return;
            }
        }
        if (pipes.isEmpty()) {
            pipes.add(new Pipe(command));
            return;
        }
        pipes.add(new Pipe(pipes.getLast(), command));
    }

    /**
     * Consequently runs all commands passing output of the previous command
     * to the next one as input
     */
    public void run() throws IOException {
        for (Pipe pc : pipes) {
            pc.run();
        }
    }
}
