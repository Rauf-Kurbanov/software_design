package ru.spbau.mit.kurbanov.contollers;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import ru.spbau.mit.kurbanov.model.Chat;
import ru.spbau.mit.kurbanov.model.Messenger;
import ru.spbau.mit.kurbanov.view.ChatView;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Messenger window controller
 */
@RequiredArgsConstructor
public class MessengerController {

    @FXML
    private Button editButton;

    @FXML
    private Button createButton;

    @FXML
    private Text userName;

    @FXML
    private TabPane chatLayout;

    private final Messenger messenger;
    private final String nameString;

    private void editName(Label label) {
        final TextInputDialog dialog = new TextInputDialog(label.getText());
        dialog.setTitle("Edit name");
        dialog.setHeaderText("");
        dialog.setContentText("Please enter your name:");
        dialog.showAndWait().ifPresent(name -> {
            label.setText(name);
            messenger.setUserName(name);
            userName.setText(name);
        });
    }

    private void createChat(Stage stage) throws IOException {
        final Stage dialog = new Stage();
        dialog.setTitle("Create chat");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-chat.fxml"));
        loader.setController(new CreateChatController(messenger, dialog));
        final Parent root2;
        root2 = loader.load();
        final CreateChatController ccController = loader.getController();
        ccController.setup();

        dialog.setScene(new Scene(root2));
        dialog.show();
    }

    private void bindTabs(TabPane chatLayout) {
        messenger.getChats().addListener((MapChangeListener<InetSocketAddress, Chat>) change -> Platform.runLater(() -> {
            if (change.wasAdded()) {
                ChatView chatView = new ChatView(change.getValueAdded());
                chatView.setOnClosed(event -> messenger.getChats().remove(change.getKey()));
                chatLayout.getTabs().add(chatView);
            } else {
                chatLayout.getTabs().removeIf(tab -> tab instanceof ChatView &&
                        ((ChatView) tab).getChat() == change.getValueRemoved());
            }
        }));
    }

    /**
     * Initalize all window logic
     */
    public void setup(Stage stage) {
        editButton.setOnMouseClicked(event -> editName(new Label(nameString)));
        createButton.setOnMouseClicked(event -> {
            try {
                createChat(stage);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });

        bindTabs(chatLayout);
        stage.setTitle("Messenger");
        stage.show();
    }
}