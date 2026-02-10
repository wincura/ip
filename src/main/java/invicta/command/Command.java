package invicta.command;

// Imports to use invicta app components and task list to execute operations
import invicta.app.InvictaException;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.TaskList;

public abstract class Command {
    public abstract void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException;

    public static boolean isExit(Command c) {
        return c instanceof ExitCommand;
    }
}
