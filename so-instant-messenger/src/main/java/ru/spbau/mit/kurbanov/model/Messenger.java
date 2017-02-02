package ru.spbau.mit.kurbanov.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;
import ru.spbau.mit.kurbanov.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Manages all chats
 */
@Slf4j
public class Messenger {

    @Getter
    private final ObservableMap<InetSocketAddress, Chat> chats = FXCollections.observableHashMap();
    private int serverPort;
    private Server server;
    private String userName;

    /**
     * Starts socket server at given port and sets handler for receiving messages.
     */
    public void startServer(int port) throws IOException {
        serverPort = port;
        server = new Server(port);
        server.setMessageHandler(this::onMessageReceived);
    }

    /**
     * Creates chat with given address.
     * If there is already a chat with such an address, nothing is created.
     */
    public void createChat(InetSocketAddress address) throws IOException {
        if (chats.containsKey(address)) {
            log.info("Already contains chat with this address");
            return;
        }
        log.info("Creating chat: address = " + address);
        final Chat chat = new Chat(address, serverPort, userName);
        chats.put(address, chat);
    }

    /**
     * Changes current user name and updates it in all active chats.
     */
    public void setUserName(String userName) {
        this.userName = userName;
        chats.values().forEach(chat -> chat.setUserName(userName));
    }

    /**
     * Handles received messages and passes them to corresponding chats.
     * If no active chat is found, new one is created using sender address.
     */
    public synchronized void onMessageReceived(InetAddress inetAddress, ChatMessage message) {
        final InetSocketAddress address = new InetSocketAddress(inetAddress, message.getServerPort());
        log.info("Received message from " + message.getName());
        Chat chat = chats.get(address);
        if (chat == null) {
            chat = new Chat(address, serverPort, userName);
            chats.put(address, chat);
        }
        chat.addReceivedMessage(message);
    }

    /**
     * Stops server.
     */
    public void shutdown() {
        if (server != null) {
            server.shutdown();
        }
    }
}
