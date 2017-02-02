package ru.spbau.mit.kurbanov.protocol;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.junit.Assert.assertEquals;

public class ChatProtocolTest {
    @Test
    public void testProtocol() throws Exception {
        final ChatMessage message = new ChatMessage("1", "aaa", 80);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        ChatProtocol.sendMessage(message, new DataOutputStream(output));
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        final ChatMessage received = ChatProtocol.receiveMessage(new DataInputStream(input));

        assertEquals(received.toString(), message.toString()); // because of bag in lombok equals for @Data
    }
}