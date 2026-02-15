package invicta.gui;

import java.io.IOException;

import invicta.InvictaBot;
import invicta.app.Message;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for InvictaBot using FXML.
 */
public class Main extends Application {

    private InvictaBot invictaBot = new InvictaBot();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            stage.setTitle("InvictaBot");

            Image icon = new Image("/images/icon.png");
            stage.getIcons().add(icon);


            stage.setScene(scene);
            stage.setMinHeight(700);
            stage.setMinWidth(500);
            Message.setGuiMode(true);
            fxmlLoader.<MainWindow>getController().initialize(invictaBot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
