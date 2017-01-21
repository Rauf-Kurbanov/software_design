package com.spbau.kurbanov.sd.shell.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.spbau.kurbanov.sd.shell.env.Environment;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        if (fileNames.isEmpty()) {
            IOUtils.copy(in, out);
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
