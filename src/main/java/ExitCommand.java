public class ExitCommand extends Command {
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        ui.bye();
    }
}
