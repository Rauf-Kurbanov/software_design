package ru.spbau.mit.kurbanov.view;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.spbau.mit.kurbanov.model.Chat;
import ru.spbau.mit.kurbanov.protocol.ChatMessage;

import java.io.IOException;

/**
 * Custom tab fom chat
 */
@Slf4j
public class ChatView extends Tab {

    @Getter
    private final Chat chat;

    public ChatView(Chat chat) {
        this.chat = chat;

        final ListView<ChatMessage> messages = new ListView<>();
        messages.getItems().addAll(chat.getMessages());
        chat.getMessages().addListener((ListChangeListener<ChatMessage>) listener ->
                Platform.runLater(() -> {
                    messages.getItems().clear();
                    messages.getItems().addAll(chat.getMessages());
                })
        );
        messages.setCellFactory(p -> new MessageView());
        final TextArea messageArea = new TextArea();
        messageArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isControlDown() || event.isMetaDown()) {
                    messageArea.setText(messageArea.getText() + "\n");
                    messageArea.end();
                    return;
                }
                try {
                    chat.sendMessage(messageArea.getText());
                    Platform.runLater(() -> messageArea.setText(""));
                } catch (IOException e) {
                    log.error("Failed sending message: " + e.getMessage());
                }
            }
        });
        setText(chat.getFriendName().get());
        chat.getFriendName().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> setText(newValue))));
        setContent(new VBox(messages, messageArea));
    }
}
