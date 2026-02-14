package invicta.command;

import java.io.IOException;

// Imports to use invicta app components and task list to execute operations
import invicta.app.InvictaException;
import invicta.app.Message;
import invicta.app.MessageKey;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.Task;
import invicta.task.TaskList;


/**
 * Represents a command that makes changes to tasks in task list.
 */
public class EditCommand extends Command {
    private CommandType commandType;
    private int index;

    /**
     * Constructs an instance of EditCommand class.
     */
    public EditCommand(CommandType commandType, int index) {
        this.commandType = commandType;
        this.index = index;
    }

    /**
     * Processes the unmark operation accordingly.
     */
    private void processUnmark(TaskList taskList, Ui ui, Storage storage) throws InvictaException, IOException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_INDEX,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
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
    private void processMark(TaskList taskList, Ui ui, Storage storage) throws InvictaException, IOException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_INDEX,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        }
        if (taskList.getDone(this.index)) {
            ui.marked(2, taskList.getTaskString(this.index));
        } else {
            taskList.setDone(this.index, true);
            storage.update(taskList);
            ui.marked(1, taskList.getTaskString(this.index));
        }
    }

    /**
     * Processes the delete operation accordingly.
     */
    private void processDelete(TaskList taskList, Ui ui, Storage storage) throws InvictaException, IOException {
        if (this.index < 0 | this.index > taskList.getSize() - 1) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_INDEX,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        }
        Task deleteTask = taskList.getTaskList().get(this.index);
        taskList.removeTask(this.index);
        storage.update(taskList);
        ui.deleted(deleteTask, taskList);
    }

    /**
     * Executes command to edit tasks in respective ways.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException, IOException {
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
        default:
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_COMMAND));
        }
    }
}
