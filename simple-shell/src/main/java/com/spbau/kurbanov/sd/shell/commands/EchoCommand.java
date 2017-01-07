package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command to displays a line of text
 */
@Parameters(commandDescription = "display a line of text")
public class EchoCommand implements Command {

    @Parameter(description = "Strings to echo")
    private final List<String> strings = new ArrayList<>();


    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        final String output = strings.stream().collect(Collectors.joining(" "));
        out.write(output.getBytes());
        out.write(System.lineSeparator().getBytes());
        final OutputStreamWriter osw = new OutputStreamWriter(out);

//        final String EOT = "\u0004";
//        final String EOT = Character.toString ((char) 4);

//        osw.write(EOT);
//        new DataOutputStream(out).writeChar((char) 4);
//        out.write(-1);
//        osw.write();
//        out.write(new byte[]{-1});
    }
}
