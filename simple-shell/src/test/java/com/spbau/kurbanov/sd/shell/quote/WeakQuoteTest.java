package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WeakQuoteTest  {
    @Test
    public void simpleExpression() {
        final Quote quote = new WeakQuote("value");
        final Environment environment = Environment.INSTANCE;
        assertEquals("value", quote.substitute(environment));
    }

    @Test
    public void singleVariableExpression() {
        final Quote quote = new WeakQuote("$VALUE");
        final Environment environment = Environment.INSTANCE;
        environment.putVariableValue("VALUE", "YYY");
        assertNotEquals("$VALUE", quote.substitute(environment));
        assertEquals("YYY", quote.substitute(environment));
    }

    @Test
    public void fewVariableExpression() {
        final Quote quote = new WeakQuote("$VALUE$X");
        final Environment environment = Environment.INSTANCE;
        environment.putVariableValue("VALUE", "YYY");
        environment.putVariableValue("X", "ZZZ");
        assertEquals("YYYZZZ", quote.substitute(environment));
    }

    @Test
    public void variablesWithoutValueExpression() {
        final Quote quote = new WeakQuote("$VALUE$X");
        final Environment environment = Environment.getEmptyEnvironment();
        assertEquals("", quote.substitute(environment));
    }
}
