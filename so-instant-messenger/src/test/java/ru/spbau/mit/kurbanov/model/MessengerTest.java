package ru.spbau.mit.kurbanov.model;

import org.junit.Test;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;

import java.net.InetSocketAddress;

import static org.junit.Assert.assertEquals;

public class MessengerTest {
    @Test
    public void testSimpleChat() throws Exception {
        final Messenger messenger1 = new Messenger();
        messenger1.setUserName("1");
        int port1 = 10574;
        messenger1.startServer(port1);
        final InetSocketAddress address1 = new InetSocketAddress("localhost", port1);

        final Messenger messenger2 = new Messenger();
        messenger2.setUserName("2");
        int port2 = 10575;
        messenger2.startServer(port2);
        InetSocketAddress address2 = new InetSocketAddress("localhost", port2);

        messenger1.createChat(address2);
        messenger1.getChats().get(address2).sendMessage("Hello");

        Thread.sleep(500);

        final Chat chat = messenger2.getChats().get(address1);
        final ChatMessage message = chat.getMessages().get(0);

        assertEquals("1", message.getName());
        assertEquals("Hello", message.getMessage());
        assertEquals(port1, message.getServerPort());
    }

}