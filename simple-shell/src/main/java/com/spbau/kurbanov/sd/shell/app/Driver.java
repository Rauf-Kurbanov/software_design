package com.spbau.kurbanov.sd.shell.app;

import com.beust.jcommander.ParameterException;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Application runner
 */
public class Driver {

    /**
     * App entry point
     * @param args Command tokens split by spaces
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final QuoteParser qp = new QuoteParser();
        final Environment env = Environment.INSTANCE;

        final Scanner scanner = new Scanner(System.in);
        System.out.printf(">> ");
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            try {
                final List<List<Quote>> quotesByPipes = qp.parse(line);
                final List<String[]> unquoted = new ArrayList<>();
                for (List<Quote> qList : quotesByPipes) {
                    unquoted.add(qList.stream()
                            .map(q -> q.substitute(env))
                            .toArray(String[]::new));
                }
                final PipeRunner pipeRunner = new PipeRunner(unquoted);

                pipeRunner.run();
            } catch (ParameterException | NoSuchFileException e) {
                System.out.println(e.getMessage());
            }
            System.out.printf("\n>> ");
        }
    }
}
