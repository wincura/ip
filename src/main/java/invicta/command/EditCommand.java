package invicta.command;

import invicta.app.InvictaException;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.TaskList;

public class EditCommand extends Command {
    private CommandType commandType;
    private int index;

    public EditCommand(CommandType commandType, int index) {
        this.commandType = commandType;
        this.index = index;
    }

    public void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException {
        switch (this.commandType) {
            case UNMARK: {
                if (index < 0 | index > taskList.getSize() - 1) {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                            + "_".repeat(100));
                } else {
                    if (!taskList.getDone(index)) {
                        ui.marked(4, taskList.getTaskString(index));
                    } else {
                        taskList.setDone(index, false);
                        storage.updateTaskListFile(taskList);
                        ui.marked(3, taskList.getTaskString(index));
                    }
                }
                break;
            }
            case MARK: {
                if (index < 0 | index > taskList.getSize() - 1) {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                            + "_".repeat(100));
                } else {
                    if (taskList.getDone(index)) {
                        ui.marked(2, taskList.getTaskString(index));
                    } else {
                        taskList.setDone(index,true);
                        storage.updateTaskListFile(taskList);
                        ui.marked(1, taskList.getTaskString(index));
                    }
                }
                break;
            }
            case DELETE: {
                if (index < 0 | index > taskList.getSize() - 1) {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tYou want me to do what? "
                            + "Put a valid index! (check task list using 'list' command)\n"
                            + "_".repeat(100));
                } else {
                    String deleteTask = taskList.getTaskString(index);
                    taskList.removeTask(index);
                    storage.updateTaskListFile(taskList);
                    System.out.println("_".repeat(100)
                            + "\n\tInto the trash! This task has been deleted: \n"
                            + "\t\t" + deleteTask + "\n\tYou've got " + taskList.getSize()
                            + " tasks in your list now.\n"
                            + "_".repeat(100));
                }
                break;
            }
        }
    }
}
