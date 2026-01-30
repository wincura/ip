public class AddCommand extends Command {
    private Task toAdd;

    public AddCommand(Task toAdd) {
        this.toAdd = toAdd;
    }

    public void execute(TaskList taskList, Storage storage, Ui ui) {
        taskList.addTask(toAdd);
        storage.updateTaskListFile(taskList);
        ui.added(toAdd, taskList);
    }
}
