package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.jetbrains.annotations.NotNull;

/**
 * Common interface for objects which can make some substitution
 * in the passed environment
 */
public interface Quote {

    /**
     * Substitute expression according with the {@code environment}
     *
     * @param environment The current execution environment
     * @return The result of substitution
     */
    @NotNull
    String substitute(Environment environment);
}
