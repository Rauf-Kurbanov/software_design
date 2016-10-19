package env;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, String> myVariables = new HashMap<>();
    private final Path myCurrentDirectory;

    private Environment() {
        myCurrentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public static final Environment INSTANCE = new Environment();

    @NotNull
    public String getVariableValue(@NotNull String variable) {
        if (myVariables.containsKey(variable)) {
            return myVariables.get(variable);
        }

        return System.getenv().getOrDefault(variable, "");
    }

    @NotNull
    public Path getCurrentDirectory() {
        return myCurrentDirectory;
    }
}
