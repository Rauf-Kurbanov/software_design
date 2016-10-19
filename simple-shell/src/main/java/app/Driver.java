package app;

import com.beust.jcommander.ParameterException;
import env.Environment;
import quote.QuoteParser;
import piping.PipeRunner;
import quote.Quote;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) throws IOException {
        QuoteParser qp = new QuoteParser();
        Environment env = Environment.INSTANCE;

        Scanner scanner = new Scanner(System.in);
        System.out.printf(">> ");
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            try {
                List<List<Quote>> quotesByPipes = qp.parse(line);
                List<String[]> unquoted = new ArrayList<>();
                for (List<Quote> qList : quotesByPipes) {
                    unquoted.add(qList.stream()
                            .map(q -> q.substitute(env))
                            .toArray(String[]::new));
                }
                PipeRunner pipeRunner = new PipeRunner(unquoted);

                pipeRunner.run();
            } catch (ParameterException | NoSuchFileException e) {
                System.out.println(e.getMessage());
            }
            System.out.printf("\n>> ");
        }
    }
}
