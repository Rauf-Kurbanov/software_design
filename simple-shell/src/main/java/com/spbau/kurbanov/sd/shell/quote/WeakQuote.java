package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.jetbrains.annotations.NotNull;

/**
 * The quote which replaces all entries of variables for their values in
 * current environment
 *
 * @see Quote
 * @see Environment
 */
public class WeakQuote implements Quote {
    private static final char VARIABLE_PREFIX = '$';
    private final String myString;

    /** Constructs a new weak quote
     * @param string The string for substitution
     */
    public WeakQuote(@NotNull String string) {
        myString = string;
    }

    /**
     * Replace all entries of {@code environment} variables for their values
     * @param environment The current execution environment
     * @return The string after all substitutions
     */
    @NotNull
    @Override
    public String substitute(Environment environment) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < myString.length()) {
            if (myString.charAt(i) == VARIABLE_PREFIX && i + 1 < myString.length() &&
                    Character.isLetter(myString.charAt(i + 1))) {
                int start = i + 1;
                i += 2;
                while (i < myString.length() && Character.isLetterOrDigit(myString.charAt(i))) {
                    i++;
                }

                String variableName = myString.substring(start, i);
                sb.append(environment.getVariableValue(variableName));
            } else {
                sb.append(myString.charAt(i++));
            }
        }

        return sb.toString();
    }

}
