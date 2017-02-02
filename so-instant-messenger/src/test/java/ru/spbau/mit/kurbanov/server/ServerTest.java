package ru.spbau.mit.kurbanov.server;

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;
import ru.spbau.mit.kurbanov.protocol.ChatProtocol;

import java.io.DataOutputStream;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

public class ServerTest {
    @Test
    public void testServer() throws Exception {
        final int port = 10765;
        final Server server = new Server(port);
        final boolean[] received = {false};

        final ChatMessage message = new ChatMessage("a", "a", 1);
        server.setMessageHandler((address, receivedMessage) -> {
            received[0] = true;
            Assert.assertEquals(message, receivedMessage);
        });

        try (Socket socket = new Socket("localhost", port)) {
            ChatProtocol.sendMessage(message, new DataOutputStream(socket.getOutputStream()));
        }

        Thread.sleep(100);
        assertTrue(received[0]);
    }
}