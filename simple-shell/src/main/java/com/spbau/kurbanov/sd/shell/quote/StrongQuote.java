package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.jetbrains.annotations.NotNull;

/**
 * Free-substitution com.spbau.kurbanov.sd.shell.quote
 *
 * @see Quote
 */
public class StrongQuote implements Quote {

    private final String myString;

    public StrongQuote(@NotNull String string) {
        myString = string;
    }

    @NotNull
    @Override
    public String substitute(Environment environment) {
        return myString;
    }
}
