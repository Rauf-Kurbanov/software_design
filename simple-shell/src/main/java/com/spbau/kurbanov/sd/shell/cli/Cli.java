package com.spbau.kurbanov.sd.shell.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.spbau.kurbanov.sd.shell.commands.*;

import java.io.IOException;

/**
 * Adaptor for JCommander
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
     * @param tokenized
     * @return
     * @throws IOException
     */
    public static Command fromTokenized(String[] tokenized) throws IOException {
        init();
        try {
            jCommander.parse(tokenized);
        } catch (ParameterException e) {
            return new ExternalCommand(tokenized);
        }

        String parsedCommand = jCommander.getParsedCommand();
        JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
        return (Command) parsedJCommander.getObjects().get(0);
    }

}