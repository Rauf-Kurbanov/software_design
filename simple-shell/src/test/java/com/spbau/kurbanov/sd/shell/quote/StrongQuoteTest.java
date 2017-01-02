package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StrongQuoteTest {

    @Test
    public void simpleExpression() {
        final Quote quote = new StrongQuote("value");
        final Environment environment = Environment.INSTANCE;
        assertEquals("value", quote.substitute(environment));
    }

    @Test
    public void singleVariableExpression() {
        final Quote quote = new StrongQuote("$VALUE");
        final Environment environment = Environment.INSTANCE;
        environment.putVariableValue("VALUE", "YYY");
        assertNotEquals("YYY", quote.substitute(environment));
        assertEquals("$VALUE", quote.substitute(environment));
    }

    @Test
    public void fewVariableExpression() {
        final Quote quote = new StrongQuote("$VALUE$X");
        final Environment environment = Environment.INSTANCE;
        environment.putVariableValue("VALUE", "YYY");
        environment.putVariableValue("Y", "ZZZ");
        assertEquals("$VALUE$X", quote.substitute(environment));
    }
}
