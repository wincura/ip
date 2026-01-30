public abstract class Command {
    public abstract void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException;

    public static boolean isExit(Command c) {
        return c instanceof ExitCommand;
    }
}
