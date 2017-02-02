package ru.spbau.mit.kurbanov.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;
import ru.spbau.mit.kurbanov.protocol.ChatProtocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Manages sigle p2p chat
 */
@Slf4j
public class Chat {

    private final InetSocketAddress address;
    private final int localPort;
    @Getter
    private final ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
    @Setter
    private String userName;
    @Getter
    private StringProperty friendName = new SimpleStringProperty("Unknown");

    /**
     * Creates a new chat.
     *
     * @param address   address of remote server to send messages
     * @param localPort port at which local server is running
     * @param userName  name of local user
     */
    public Chat(InetSocketAddress address, int localPort, String userName) {
        this.address = address;
        this.localPort = localPort;
        this.userName = userName;
    }

    /**
     * Adds received message to chat history and updates friend name in case
     * it was changed or not initialized.
     */
    public void addReceivedMessage(ChatMessage message) {
        messages.add(message);
        friendName.set(message.getName());
    }

    /**
     * Sends message with given text through tcp-ip protocol.
     * Message is wrapped into {@link ChatMessage} object with additional information
     * needed for displaying and future communication.
     *
     * @throws IOException if sending message failed
     */
    public void sendMessage(String text) throws IOException {
        log.info("Sending message to " + address);
        final ChatMessage message = new ChatMessage(userName, text, localPort);
        messages.add(message);
        try (Socket socket = new Socket()) {
            socket.connect(address, 1000);
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            ChatProtocol.sendMessage(message, output);
        }
    }
}
