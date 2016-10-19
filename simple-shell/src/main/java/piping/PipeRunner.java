package piping;

import commands.CatCommand;
import commands.Command;
import commands.CommandFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

    public void run() throws IOException {
        for (Pipe pc : pipes) {
            pc.run();
        }
    }
}
