package com.spbau.kurbanov.sd.shell.env;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * The environment of execution. Contains variables and current directory
 *
 */
public class Environment {
    private final Map<String, String> myVariables = new HashMap<>();
    private final Path myCurrentDirectory;

    private Environment() {
        myCurrentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    /**
     * Singleton instance of environment
     */
    public static final Environment INSTANCE = new Environment();

    /**
     * Factory method for an empty environment
     * @return instance of empty environment
     */
    public static Environment getEmptyEnvironment() {
        return new Environment() {
            private final Map<String, String> map = new HashMap<>();

            @Override
            public String getVariableValue(String variable) {
                return map.getOrDefault(variable, "");
            }

            @Override
            public void putVariableValue(String variable, String value) {
                map.put(variable, value);
            }

            @Override
            public Path getCurrentDirectory() {
                return Paths.get(System.getProperty("user.dir"));
            }
        };
    }

    /**
     * Return the value of {@code variable} by name
     *
     * @param variable The name of a variable
     * @return The value of variable if the variable existed in environment,
     * otherwise empty string
     */
    @NotNull
    public String getVariableValue(@NotNull String variable) {
        if (myVariables.containsKey(variable)) {
            return myVariables.get(variable);
        }

        return System.getenv().getOrDefault(variable, "");
    }

    /**
     * Add new variable to execution environment
     *
     * @param variable The name of variable
     * @param value    The value of {@code variable}
     */
    public void putVariableValue(@NotNull String variable, @NotNull String value) {
        myVariables.put(variable, value);
    }

    /**
     * Returns current working directory
     *
     * @return The absolute path to working directory
     */
    @NotNull
    public Path getCurrentDirectory() {
        return myCurrentDirectory;
    }

}
