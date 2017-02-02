package ru.spbau.mit.kurbanov.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.spbau.mit.kurbanov.contollers.StartController;
import ru.spbau.mit.kurbanov.model.Messenger;

/**
 * Driver class for messenger app
 */
public class MessengerApp extends Application {

    private final Messenger messenger = new Messenger();

    public static void main(String[] args) {
        launch(args);
    }

   @Override
    public void start(Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/startscreen.fxml"));
        loader.setController(new StartController());
        final Parent root = loader.load();
        final StartController controller = loader.getController();
        controller.setup(stage);

        stage.setScene(new Scene(root));
        stage.setTitle("messaging");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        messenger.shutdown();
        super.stop();
    }
}
