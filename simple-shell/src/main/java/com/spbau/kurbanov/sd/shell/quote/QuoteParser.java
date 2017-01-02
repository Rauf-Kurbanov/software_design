package com.spbau.kurbanov.sd.shell.quote;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for parsing quotes
 */
public class QuoteParser {
    private static final char NO_QUOTE = '#';
    private static final char STRONG_QUOTE = '\'';
    private static final char WEAK_QUOTE = '"';

    @NotNull
    public List<List<Quote>> parse(@NotNull String input) {
        List<List<Quote>> result = new ArrayList<>();
        final List<MyQuoteDescriptor> quotes = findQuotes(input);

        List<String> commands = splitWithQuotes(input, quotes, '|');
        for (String command : commands) {
            List<MyQuoteDescriptor> commandQuotes = findQuotes(command);
            List<String> parts = splitWithQuotes(command, commandQuotes, ' ').stream()
                    .filter(x -> !x.trim().isEmpty()).collect(Collectors.toList());

            List<Quote> commandResult = new ArrayList<>();
            for (String word : parts) {
                List<MyQuoteDescriptor> wordQuotes = findQuotes(word);

                int lastProceedIx = 0;
                CompositeQuote quote = new CompositeQuote();
                for (MyQuoteDescriptor descriptor : wordQuotes) {
                    if (descriptor.from != lastProceedIx) {
                        quote.append(new WeakQuote(word.substring(lastProceedIx, descriptor.from)));
                    }

                    quote.append(descriptor.toQuote());
                    lastProceedIx = descriptor.to + 1;
                }

                if (lastProceedIx < word.length()) {
                    quote.append(new WeakQuote(word.substring(lastProceedIx)));
                }

                commandResult.add(quote);
            }

            result.add(commandResult);
        }

        return result;
    }

    private static List<MyQuoteDescriptor> findQuotes(@NotNull String input) {
        char lastQuote = NO_QUOTE;
        int lastQuoteIx = -1;
        List<MyQuoteDescriptor> quotes = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (lastQuote == NO_QUOTE) {
                lastQuote = isQuote(currentChar) ? currentChar : lastQuote;
                lastQuoteIx = i;
            } else if (isQuote(currentChar) && currentChar == lastQuote) {
                MyQuoteDescriptor quote = currentChar == STRONG_QUOTE
                        ? new MyQuoteDescriptor(input, MyQuoteType.STRONG, lastQuoteIx, i)
                        : new MyQuoteDescriptor(input, MyQuoteType.WEAK, lastQuoteIx, i);
                quotes.add(quote);

                lastQuote = NO_QUOTE;
                lastQuoteIx = -1;
            }
        }

        return quotes;
    }

    private static List<String> splitWithQuotes(@NotNull String input,
                                                @NotNull List<MyQuoteDescriptor> quotes, char splitChar) {
        int lastDelimiterIx = -1;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            int ix = i;
            if (input.charAt(i) == splitChar && quotes.stream().allMatch(quoteBase -> !quoteBase.containsPosition(ix))) {
                result.add(input.substring(lastDelimiterIx + 1, i));
                lastDelimiterIx = i;
            }
        }

        result.add(input.substring(lastDelimiterIx + 1));

        return result;
    }

    private static boolean isQuote(char ch) {
        return STRONG_QUOTE == ch || WEAK_QUOTE == ch;
    }

    private enum MyQuoteType {
        WEAK, STRONG
    }

    private static class MyQuoteDescriptor {
        private final String myString;
        final MyQuoteType type;
        final int from;
        final int to;

        MyQuoteDescriptor(@NotNull String string, @NotNull MyQuoteType quoteType, int begin, int end) {
            myString = string;
            type = quoteType;
            from = begin;
            to = end;
        }

        boolean containsPosition(int ix) {
            return from <= ix && ix <= to;
        }

        Quote toQuote() {
            String body = myString.substring(from + 1, to);
            if (type == MyQuoteType.WEAK) {
                return new WeakQuote(body);
            }

            return new StrongQuote(body);
        }
    }
}
