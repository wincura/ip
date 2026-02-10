package invicta.command;

import invicta.app.InvictaException;
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
