package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Print lines matching a pattern
 */
public class GrepCommand implements Command {

    @Parameter(names = "-i", description = "Ignore case distinctions in both the pattern and the input files.")
    private Boolean ignoreCase = false;

    @Parameter(names = "-w", description = "Select  only  those  lines  containing  matches  that  form  whole  words.")
    private Boolean wordRegexp = false;

    @Parameter(names = "-A", description = "Print N lines of trailing context after matching lines.")
    private Integer afterContext = 0;

    @Parameter(description = "Pattern")
    private List<String> inputs = new ArrayList<>();

    private Map<String, List<String>> matches = new HashMap<>();

    private void print(@NotNull OutputStream out) throws IOException {
        for (String fileName : matches.keySet()) {
            for (String line : matches.get(fileName)) {
                out.write((fileName + line + "\n").getBytes());
            }
        }
    }

    private void grep(String filePrefix, InputStream input, Pattern pattern) throws IOException {
        final BufferedReader it = new BufferedReader(new InputStreamReader(input));
        if (!matches.containsKey(filePrefix)) {
            matches.put(filePrefix, new ArrayList<>());
        }
        final List<String> res = matches.get(filePrefix);

        String line;
        int nLinesAfter = 0;
        while ((line = it.readLine()) != null) {
            if (pattern.matcher(line).find()) {
                nLinesAfter = afterContext;
                res.add(line);
                continue;
            }
            if (nLinesAfter-- > 0)
                res.add(line);
        }
    }

    private Pattern buildPattern() {
        String p = inputs.get(0);
        if (wordRegexp) {
            p = "(\\s|^)" + p + "(\\s|$)";
        }
        if (ignoreCase) {
            return Pattern.compile(p, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(p);
    }

    @Override
    public void run(@NotNull InputStream in, @NotNull OutputStream out, @NotNull OutputStream err) throws IOException {
        final Pattern pattern = buildPattern();
        if (inputs.size() < 1)
            return;
        if (inputs.size() == 1) {
            grep("", in, pattern);
        } else {
            for (int i = 1; i < inputs.size(); ++i) {
                final String fn = inputs.get(i);
                grep(fn + ": ", new FileInputStream(new File(fn).getAbsoluteFile()), pattern);
            }
        }

        print(out);
    }
}
