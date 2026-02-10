package invicta.command;

// Imports to use invicta app components and task list to execute operations
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.TaskList;

/**
 * Represents a command that displays an exit message upon exiting.
 */
public class ExitCommand extends Command {
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.bye();
    }
}
