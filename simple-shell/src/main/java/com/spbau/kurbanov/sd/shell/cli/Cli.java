package com.spbau.kurbanov.sd.shell.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.spbau.kurbanov.sd.shell.commands.*;

import java.io.IOException;

/**
 * Adaptor for JCommander extended by a coupe of non-standard commands
 */
public class Cli {
    private static final JCommander jCommander = new JCommander(new Cli());

    private Cli() {}

    private static void init()  {
        jCommander.addCommand("cat", new CatCommand());
        jCommander.addCommand("echo", new EchoCommand());
        jCommander.addCommand("exit", new ExitCommand());
        jCommander.addCommand("pwd", new PwdCommand());
        jCommander.addCommand("wc", new WcCommand());
    }

    /**
     * Creates command from tokenized string
     */
    public static Command fromTokenized(String[] tokenized) throws IOException {
        init();
        try {
            jCommander.parse(tokenized);
        } catch (ParameterException e) {
            if (tokenized.length == 1 && AssignCommand.isAssignExpression(tokenized[0])) {
                return new AssignCommand(tokenized[0]);
            }
            return new ExternalCommand(tokenized);
        }

        final String parsedCommand = jCommander.getParsedCommand();
        final JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
        return (Command) parsedJCommander.getObjects().get(0);
    }

}