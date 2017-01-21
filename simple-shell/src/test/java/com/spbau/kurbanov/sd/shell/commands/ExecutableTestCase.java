package com.spbau.kurbanov.sd.shell.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ExecutableTestCase {

    protected static final InputStream EMPTY_INPUT_STREAM = new InputStream() {
        @Override
        public int read() throws IOException {
            return -1;
        }
    };

    @SuppressWarnings("WeakerAccess")
    protected List<Byte> outputData;
    protected OutputStream outputStream;

    @SuppressWarnings("WeakerAccess")
    protected List<Byte> errorData;
    protected OutputStream errorStream;

    @Before
    public void initStreams() {
        outputData = new ArrayList<>();
        outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputData.add((byte) b);
            }
        };

        errorData = new ArrayList<>();
        errorStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                errorData.add((byte) b);
            }
        };
    }

    protected String getOutputString() {
        return new String(ArrayUtils.toPrimitive(outputData.toArray(new Byte[outputData.size()])));
    }

    protected String getErrorString() {
        return new String(ArrayUtils.toPrimitive(errorData.toArray(new Byte[errorData.size()])));
    }
}
