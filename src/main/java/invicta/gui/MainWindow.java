package invicta.gui;

import invicta.InvictaBot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private InvictaBot invictaBot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/sergeant_normaund.png"));
    private Image invictaImage = new Image(this.getClass().getResourceAsStream("/images/chevalier_salade.png"));
    private Image buttonImage = new Image(this.getClass().getResourceAsStream("/images/send.png"));

    /**
     * Initializes the GUI by setting invictaBot object and displaying the startup message.
     */
    @FXML
    public void initialize(InvictaBot invictaBot) {
        this.invictaBot = invictaBot;
        // Show first message here
        dialogContainer.getChildren().add(
                DialogBox.getInvictaDialog(
                        invictaBot.getStartupMessage(),
                        invictaImage
                )
        );
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing InvictaBot's reply and then
     * appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = invictaBot.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getInvictaDialog(response, invictaImage)
        );
        userInput.clear();

        if (invictaBot.isExit()) {
            javafx.application.Platform.exit();
        }
    }
}
