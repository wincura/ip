package invicta;
// CS2103T Individual Project by William Scott Win A0273291A

// Imports to handle files and user input
import java.io.IOException;
import java.util.Scanner;

// Imports to handle time data
import java.time.format.DateTimeParseException;

// Imports to use packages of invicta
import invicta.app.InvictaException;
import invicta.app.Parser;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.command.Command;
import invicta.command.CommandType;
import invicta.task.TaskList;

/**
 * Represents the main entry point of InvictaBot.
 * Coordinates user interactions, command parsing and command execution.
 */
public class InvictaBot {
    // data structures and file paths to be used by
    private static final String TASK_LIST_FILE_PATH = "./data/tasklist.txt";

    // More OOP Objects
    private Ui invictaUi;
    private Storage invictaStorage;
    private TaskList invictaTasks;

    public InvictaBot() {
        try {
            this.invictaUi = new Ui();
            this.invictaStorage = new Storage(TASK_LIST_FILE_PATH);
            invictaTasks = new TaskList(invictaStorage.load());
        } catch (Exception e) {
            assert invictaUi != null;
            invictaUi.showException(e);
            invictaTasks = new TaskList();
        }
    }

    /**
     * Processes the user input and command execution.
     * Core functionality of the chatbot.
     *
     * @param s Scanner to accept user input.
     * @param ui Ui component of chatbot.
     * @param storage Storage component of chatbot.
     * @param tasks Task List of chatbot.
     */
    public void processCommand(Scanner s, Ui ui, Storage storage, TaskList tasks, boolean isExit) {
        while (!isExit) {
            try {
                String[] commandString = ui.readCommand(s);
                CommandType commandType = CommandType.fromString(commandString[0]);
                Command c = Parser.parseCommandData(commandString, commandType);
                c.execute(tasks, storage, ui);
                isExit = Command.isExit(c);
            } catch (Exception e) {
                ui.showException(e);
            }
        }
    }

    /**
     * Runs the app.
     * Accepts user input for username, then user input for commands.
     * Parses the commands based on command type, and executes the command.
     */
    public void run() {
        Scanner s = new Scanner(System.in);
        boolean isExit = false;
        invictaUi.logo();
        invictaUi.readUsername(s);
        processCommand(s, invictaUi, invictaStorage, invictaTasks, isExit);
    }

    /**
     * Main method of InvictaBot and executes the run method.
     */
    public static void main(String[] args) {
        new InvictaBot().run();
    }
}
