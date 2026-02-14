package invicta.command;

import java.io.IOException;

// Imports to use invicta app components and task list to execute operations
import invicta.app.InvictaException;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.Task;
import invicta.task.TaskList;



/**
 * Represents a command that adds tasks to task list.
 */
public class AddCommand extends Command {
    private Task toAdd;

    public AddCommand(Task toAdd) {
        this.toAdd = toAdd;
    }

    /**
     * Executes command to add tasks of respective types.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException, IOException {
        taskList.addTask(toAdd);
        storage.update(taskList);
        ui.added(toAdd, taskList);
    }
}
