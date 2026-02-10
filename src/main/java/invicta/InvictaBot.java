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


public class InvictaBot {
    // data structures and file paths to be used by
    private static final String TASK_LIST_FILE_PATH = "./data/tasklist.txt";

    // More OOP Objects
    private Ui invictaUi;;
    private Storage invictaStorage;
    private TaskList invictaTasks;

    public InvictaBot(String filePath) {
        try {
            this.invictaUi = new Ui();
            this.invictaStorage = new Storage(TASK_LIST_FILE_PATH);
            invictaTasks = new TaskList(invictaStorage.load());
        } catch (IOException e) {
            invictaUi.showLoadingError(e);
            invictaTasks = new TaskList();
        } catch (InvictaException e) {
            invictaUi.showException(e);
            invictaTasks = new TaskList();
        }
    }

    public void run() {
        Scanner s = new Scanner(System.in);
        boolean isExit = false;
        // Loop to prompt user for username input until valid
        invictaUi.logo();
        invictaUi.readUsername(s);

        while (!isExit) {
            try {
                String[] commandString = invictaUi.readCommand(s);
                CommandType commandType = CommandType.fromString(commandString[0]);
                Command c = Parser.parseCommandData(commandString, commandType);
                c.execute(invictaTasks, invictaStorage, invictaUi);
                isExit = Command.isExit(c);
            }
            catch (NumberFormatException e) {
                System.out.println("_".repeat(100)
                        + "\n\tAn index is a number, so go put one! (usage: mark/unmark <int as index>)\n"
                        + "_".repeat(100));
            } catch (DateTimeParseException e) {
                System.out.println("_".repeat(100)
                        + "\n\tInvalid date time format! Type 'help' to view acceptable formats.\n"
                        + "_".repeat(100));
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        InvictaBot invicta = new InvictaBot(TASK_LIST_FILE_PATH);
        invicta.run();
    }
}
