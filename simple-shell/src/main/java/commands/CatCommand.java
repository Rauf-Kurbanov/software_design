package commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import env.Environment;
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

@Parameters(commandDescription = "Concatenate files and print on the standard output")
public class CatCommand extends Command {

    @Getter
    @Parameter(description = "File patterns to add to the index")
    private final List<String> fileNames = new ArrayList<>();

    private static final int BUFFER_SIZE = 4096;

    private static void copy(@NotNull InputStream in, @NotNull OutputStream out)
            throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int readCount = in.read(buffer);
        while(readCount > 0) {
            out.write(buffer, 0, readCount);
            readCount = in.read(buffer);
        }
    }

    @Override
    public void run(@NotNull InputStream in,
                    @NotNull OutputStream out,
                    @NotNull OutputStream err) throws IOException {
        if (fileNames.isEmpty()) {
            IOUtils.copy(in, out);
//            copy(in, out);
//            in.close();
            return;
        }

        for (String filename : fileNames) {
            Path currentDirectory = Environment.INSTANCE.getCurrentDirectory();
            File file = currentDirectory.resolve(filename).toFile();
            if (!file.exists()) {
                err.write(String.format("File %s not found", filename).getBytes());
                err.write(System.lineSeparator().getBytes());
            }
            Files.copy(file.toPath(), out);
        }
    }
}
