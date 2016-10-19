package cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import commands.*;

import java.io.IOException;

public class Cli {
    private static JCommander jCommander;

    private Cli() {}

    private static void init()  {
        Cli cli = new Cli();
        jCommander = new JCommander(cli);
        jCommander.addCommand("cat", new CatCommand());
        jCommander.addCommand("echo", new EchoCommand());
        jCommander.addCommand("exit", new ExitCommand());
        jCommander.addCommand("pwd", new PwdCommand());
        jCommander.addCommand("wc", new WcCommand());
    }

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