package invicta.command;

import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.Task;
import invicta.task.TaskList;

public class AddCommand extends Command {
    private Task toAdd;

    public AddCommand(Task toAdd) {
        this.toAdd = toAdd;
    }

    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.addTask(toAdd);
        storage.update(taskList);
        ui.added(toAdd, taskList);
    }
}
