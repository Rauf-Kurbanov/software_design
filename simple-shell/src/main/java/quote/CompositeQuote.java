package quote;

import env.Environment;

import java.util.ArrayList;
import java.util.List;

public class CompositeQuote implements Quote {
    private final List<Quote> myQuotes = new ArrayList<>();

    @Override
    public String substitute(Environment environment) {
        StringBuilder builder = new StringBuilder();
        for (Quote quote : myQuotes) {
            builder.append(quote.substitute(environment));
        }

        return builder.toString();
    }

    public void append(Quote quote) {
        myQuotes.add(quote);
    }
}
