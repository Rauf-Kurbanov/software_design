package ru.spbau.mit.kurbanov.contollers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import ru.spbau.mit.kurbanov.model.Messenger;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Create chat window controller
 */
@RequiredArgsConstructor
public class CreateChatController {

    private final Messenger messenger;
    private final Stage dialog;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField hostTextF;

    @FXML
    private TextField portTextF;

    @FXML
    private Text stateText;

    /**
     * Initalize all window logic
     */
    public void setup() {
        createButton.setOnMouseClicked(event -> {
            try {
                InetSocketAddress address = new InetSocketAddress(hostTextF.getText(), Integer.parseInt(portTextF.getText()));
                messenger.createChat(address);
                dialog.close();
            } catch (IllegalArgumentException e) {
                stateText.setText("Incorrect argument: " + e.getMessage());
            } catch (IOException e) {
                stateText.setText("Connection failed: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                stateText.setText("Unknown error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        cancelButton.setOnMouseClicked(event -> dialog.close());
    }
}
