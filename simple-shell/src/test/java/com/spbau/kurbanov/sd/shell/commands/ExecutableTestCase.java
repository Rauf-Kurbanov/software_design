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

    protected List<Byte> myOutputData;
    protected OutputStream myOutputStream;

    protected List<Byte> myErrorData;
    protected OutputStream myErrorStream;

    @Before
    public void initStreams() {
        myOutputData = new ArrayList<>();
        myOutputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                myOutputData.add((byte) b);
            }
        };

        myErrorData = new ArrayList<>();
        myErrorStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                myErrorData.add((byte) b);
            }
        };
    }

    protected String getOutputString() {
        return new String(ArrayUtils.toPrimitive(myOutputData.toArray(new Byte[myOutputData.size()])));
    }

    protected String getErrorString() {
        return new String(ArrayUtils.toPrimitive(myErrorData.toArray(new Byte[myErrorData.size()])));
    }
}
