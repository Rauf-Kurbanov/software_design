package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.cli.Cli;

import java.io.IOException;

/**
 * Class for instantiating commands
 */
public interface CommandFactory {

    /**
     * Factory method for creating commands
     * @param line command string split by spaces
     * @return matching command instance
     */
    static Command createFromArray(String[] line) throws IOException {
        if (line == null || line.length == 0) {
            return null;
        }
        return Cli.fromTokenized(line);
    }
}
