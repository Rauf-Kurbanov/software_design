package commands;

import cli.Cli;

import java.io.IOException;

public interface CommandFactory {

    static Command createFromArray(String[] line) throws IOException {
        if (line == null || line.length == 0) {
            return null;
        }
        return Cli.fromTokenized(line);
    }
}
