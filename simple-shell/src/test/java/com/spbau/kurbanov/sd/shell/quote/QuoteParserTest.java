package com.spbau.kurbanov.sd.shell.quote;

import com.spbau.kurbanov.sd.shell.env.Environment;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class QuoteParserTest {
    private final QuoteParser myParser = new QuoteParser();

    @Test
    public void simpleStringQuote() {
        final List<List<Quote>> result = myParser.parse("cat");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        final Quote quote = result.get(0).get(0);
        assertEquals("cat", quote.substitute(Environment.getEmptyEnvironment()));
    }

    @Test
    public void pipeStringQuote() {
        final List<List<Quote>> result = myParser.parse("cat | help");
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    public void multipleParameters() {
        final List<List<Quote>> result = myParser.parse("cat ard1 ard2 | help arg | pwd");
        assertEquals(3, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(2, result.get(1).size());
        assertEquals(1, result.get(2).size());
    }

    @Test
    public void quoteParameter() {
        final List<List<Quote>> result = myParser.parse("cat \"arg1 arg2\"");
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
    }

    @Test
    public void quotedPipe() {
        final List<List<Quote>> result = myParser.parse("cat \"arg1 | arg2\" \'|\' ");
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }
}
