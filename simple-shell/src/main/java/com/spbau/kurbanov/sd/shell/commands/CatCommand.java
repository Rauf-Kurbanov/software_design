package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.spbau.kurbanov.sd.shell.env.Environment;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to concatenate files and print on the standard output
 */
@Parameters(commandDescription = "Concatenate files and print on the standard output")
public class CatCommand implements Command {

    @Getter
    @Parameter(description = "File patterns to add to the index")
    private final List<String> fileNames = new ArrayList<>();

    private static final int BUFFER_SIZE = 4096;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    private volatile int readCount = 0;

    private  static void copy(@NotNull InputStream in, @NotNull OutputStream out)
            throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int readCount = in.read(buffer);
        while (readCount > 0) {
            out.write(buffer, 0, readCount);
            readCount = in.read(buffer);
        }
    }

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        if (fileNames.isEmpty()) {
//            IOUtils.copy(in, out);
//            copy(in, out);

///////////////////////////////////////////////////////////////

//            final Thread readThread = new Thread(() -> {
//                try {
//                    readCount = in.read(buffer);
//                } catch (IOException ignored) {}
//            });
//
//            final Thread writeThread = new Thread(() -> {
//                while (readCount > 0) {
//                    try {
//                        out.write(buffer, 0, readCount);
//                        readCount = in.read(buffer);
//                    } catch (IOException ignored) {
//                    }
//                }
//            });

//            readThread.start();
//            writeThread.start();

/////////////////////////////////////////////////////////////////


            copy(in, out);
            return;
        }

            for (String filename : fileNames) {
            final Path currentDirectory = Environment.INSTANCE.getCurrentDirectory();
            final File file = currentDirectory.resolve(filename).toFile();
            if (!file.exists()) {
                err.write(String.format("File %s not found", filename).getBytes());
                err.write(System.lineSeparator().getBytes());
            }
            Files.copy(file.toPath(), out);
        }
    }
}
