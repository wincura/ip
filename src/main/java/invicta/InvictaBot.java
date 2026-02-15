package invicta;
// CS2103T Individual Project by William Scott Win A0273291A

// Imports to handle files and user input
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// Imports to use packages of invicta
import invicta.app.Message;
import invicta.app.Parser;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.command.Command;
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

    // For GUI version
    private boolean hasChosenLanguage = false;
    private boolean hasUsername = false;
    private boolean isExit = false;

    /**
     * Constructs an instance of the InvictaBot app.
     */
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
     * Displays the startup message prompting language.
     */
    public String getStartupMessage() {
        return invicta.app.Message.getChatbotMessage(invicta.app.MessageKey.PROMPT_LANGUAGE);
    }

    /**
     * Sets isExit to true, used for exiting app.
     */
    public boolean isExit() {
        return isExit;
    }


    /**
     * Gets the user input for GUI version.
     * Written with the help of AI tool, tweaked by me to improve code quality.
     */
    public String getResponse(String input) {
        return captureStdout(() -> {
            if (!hasChosenLanguage) {
                getLanguageResponse(input);
                return;
            }

            if (!hasUsername) {
                getUsernameResponse(input);
                return;
            }

            if (isExit) {
                return;
            }

            getCommandResponse(input);
        });
    }


    /**
     * Gets the user input for language for GUI version.
     * Used as helper.
     */
    private void getLanguageResponse(String input) {
        try {
            Parser.processLanguage(new Scanner(input.isEmpty() ? " " : input), invictaUi);
            hasChosenLanguage = true;

            System.out.println(invicta.app.Message.getChatbotMessage(invicta.app.MessageKey.PROMPT_USERNAME));
        } catch (Exception e) {
            invictaUi.showException(e);
        }
    }

    /**
     * Gets the user input for username for GUI version.
     * Used as helper.
     */
    private void getUsernameResponse(String input) {
        try {
            Parser.processUsername(new Scanner(input.isEmpty() ? " " : input), invictaUi);
            hasUsername = true;

            System.out.println(invicta.app.Message.getChatbotMessageFormatted(
                    invicta.app.MessageKey.PROMPT_COMMAND,
                    invictaUi.getUsername()
            ));
        } catch (Exception e) {
            invictaUi.showException(e);
        }
    }

    /**
     * Gets the user input for command for GUI version.
     * Used as helper.
     */
    private void getCommandResponse(String input) {
        try {
            Command c = Parser.parseCommandData(input);
            c.execute(invictaTasks, invictaStorage, invictaUi);
            isExit = Command.isExit(c);
        } catch (Exception e) {
            invictaUi.showException(e);
        }
    }
    /**
     * Captures the output from System.out to be displayed in GUI version.
     * Used as helper.
     * Written with the help of AI tool, tweaked by me.
     */
    private String captureStdout(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        PrintStream tempOut = new PrintStream(capturedOut, true, StandardCharsets.UTF_8);
        System.setOut(tempOut);

        try {
            action.run();
        } finally {
            System.out.flush();
            System.setOut(originalOut);
        }
        return capturedOut.toString(StandardCharsets.UTF_8).trim();
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
                Command c = ui.readCommand(s);
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
        invictaUi.readLanguage(s);
        invictaUi.logo();
        invictaUi.readUsername(s);
        processCommand(s, invictaUi, invictaStorage, invictaTasks, isExit);
    }

    /**
     * Serves as the entry point of InvictaBot and executes the run method.
     */
    public static void main(String[] args) {
        Message.setGuiMode(false);
        new InvictaBot().run();
    }
}
