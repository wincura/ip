package invicta.command;

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
