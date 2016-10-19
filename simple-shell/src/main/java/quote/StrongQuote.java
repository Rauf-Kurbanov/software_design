package quote;

import env.Environment;
import org.jetbrains.annotations.NotNull;

public class StrongQuote implements Quote {

    private final String myString;

    public StrongQuote(@NotNull String string) {
        myString = string;
    }

    @NotNull
    @Override
    public String substitute(Environment Enviroment) {
        return myString;
    }
}
