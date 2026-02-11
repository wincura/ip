package invicta.command;

// Imports to use invicta app components and task list to execute operations
import invicta.app.InvictaException;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.TaskList;

/**
 * Represents a command that makes changes to tasks in task list.
 */
public class EditCommand extends Command {
    private CommandType commandType;
    private int index;

    public EditCommand(CommandType commandType, int index) {
        this.commandType = commandType;
        this.index = index;
    }

    /**
     * Processes the unmark operation accordingly.
     */
    private void processUnmark(TaskList taskList, Ui ui, Storage storage) throws InvictaException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Ui.SEPARATOR
                    + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                    + Ui.SEPARATOR);
        }
        if (!taskList.getDone(this.index)) {
            ui.marked(4, taskList.getTaskString(this.index));
        } else {
            taskList.setDone(this.index, false);
            storage.update(taskList);
            ui.marked(3, taskList.getTaskString(this.index));
        }
    }

    /**
     * Processes the mark operation accordingly.
     */
    private void processMark(TaskList taskList, Ui ui, Storage storage) throws InvictaException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Ui.SEPARATOR
                    + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                    + Ui.SEPARATOR);
        }
        if (taskList.getDone(this.index)) {
            ui.marked(2, taskList.getTaskString(this.index));
        } else {
            taskList.setDone(this.index,true);
            storage.update(taskList);
            ui.marked(1, taskList.getTaskString(this.index));
        }
    }

    /**
     * Processes the delete operation accordingly.
     */
    private void processDelete(TaskList taskList, Ui ui, Storage storage) throws InvictaException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Ui.SEPARATOR
                    + "\n\tYou want me to do what? "
                    + "Put a valid index! (check task list using 'list' command)\n"
                    + Ui.SEPARATOR);
        }
        String deleteTask = taskList.getTaskString(this.index);
        taskList.removeTask(this.index);
        storage.update(taskList);
        System.out.println(Ui.SEPARATOR
                + "\n\tInto the trash! This task has been deleted: \n"
                + "\t\t" + deleteTask + "\n\tYou've got " + taskList.getSize()
                + " tasks in your list now.\n"
                + Ui.SEPARATOR);
    }

    /**
     * Executes command to edit tasks in respective ways.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException {
        switch (this.commandType) {
        case UNMARK: {
            processUnmark(taskList, ui, storage);
            break;
        }
        case MARK: {
            processMark(taskList, ui, storage);
            break;
        }
        case DELETE: {
            processDelete(taskList, ui, storage);
            break;
        }
        }
    }
}
