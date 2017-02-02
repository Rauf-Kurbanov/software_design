package ru.spbau.mit.kurbanov.contollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.spbau.mit.kurbanov.model.Messenger;

import java.io.IOException;

/**
 * Startscreen window controller
 */
public class StartController {

    private final Messenger messenger = new Messenger();

    @FXML
    private Button startButton;

    @FXML
    private TextField portTextF;

    @FXML
    private TextField unameTextF;

    @FXML
    private Text message;

    /**
     * Initalize all window logic
     */
    public void setup(Stage stage) throws IOException {
        startButton.setOnMouseClicked(event -> {
            final String name = unameTextF.getText();
            if (name.isEmpty()) {
                message.setText("Please enter your name");
                return;
            }
            try {
                message.setText("Creating messenger");
                messenger.setUserName(name);
                messenger.startServer(Integer.parseInt(portTextF.getText()));
            } catch (NumberFormatException e) {
                message.setText("Failed to parse port number");
            } catch (IOException e) {
                message.setText("Unknown error: " + e.getMessage());
            }
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/tab-messenger.fxml"));
        loader.setController(new MessengerController(messenger, unameTextF.getText()));
            final Parent root2;
            try {
                root2 = loader.load();
                final MessengerController mController = loader.getController();
                mController.setup(stage);

                stage.setScene(new Scene(root2));
                stage.setTitle("messenger");
                stage.show();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }
}
