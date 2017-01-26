package com.spbau.kurbanov.sd.shell.commands;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Update execution environment - add or update global variable
 * <p>usage: name=value</p>
 */
public class AssignCommand implements Command {
    private static final char ASSIGN_CHARACTER = '=';

    private final String input;

    /**
     * Construct a new assignment command
     *
     * @param input valid assignment expression
     */
    public AssignCommand(@NotNull String input) {
        this.input = input;
    }

    private static boolean isIdentifier(@NotNull String value) {
        boolean isIdentifier = !value.isEmpty() &&
                (Character.isLetter(value.charAt(0)) || value.charAt(0) == '_');

        for (int i = 1; i < value.length(); i++) {
            char ch = value.charAt(i);
            isIdentifier = isIdentifier && Character.isLetterOrDigit(ch) || ch == '_';
        }

        return isIdentifier;
    }

    /**
     * Checks that expression can be interpreted as assignment expression. Expression pattern: {@code variable=value}
     *
     * @param expression the candidate to be an expression
     * @return true is {@code expression} can be interpreted as assignment, false otherwise
     */
    public static boolean isAssignExpression(@NotNull String expression) {
        expression = expression.trim();
        int eqIndex = expression.indexOf('=');
        return eqIndex != -1 && eqIndex != expression.length() - 1
                && isIdentifier(expression.substring(0, eqIndex));
    }

    @Override
    public void run(@NotNull InputStream in, @NotNull OutputStream out, @NotNull OutputStream err) throws IOException {
        int eqIndex = input.indexOf(ASSIGN_CHARACTER);
        Environment.INSTANCE.putVariableValue(input.substring(0, eqIndex), input.substring(eqIndex + 1));
    }
}
