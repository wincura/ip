package invicta.command;

// Imports to handle time data and use data structures
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Imports to use invicta app components and task list to execute operations
import invicta.app.InvictaException;
import invicta.app.Message;
import invicta.app.MessageKey;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.Task;
import invicta.task.TaskList;


/**
 * Represents a command that displays information to user.
 */
public class DisplayCommand extends Command {
    private CommandType commandType;
    private LocalDate dateToSearch;
    private String stringToSearch;
    private LocalDateTime periodStartTime;
    private LocalDateTime periodEndTime;

    /**
     * Constructs an instance of a DisplayCommand.
     */
    public DisplayCommand(CommandType commandType) {
        this.commandType = commandType;
    }
    /**
     * Constructs an instance of a DisplayCommand, specifically for a 'day' command.
     */
    public DisplayCommand(CommandType commandType, LocalDate dateToSearch) {
        this.commandType = commandType;
        this.dateToSearch = dateToSearch;
    }
    /**
     * Constructs an instance of a DisplayCommand, specifically for a 'period' command.
     */
    public DisplayCommand(CommandType commandType, LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        this.commandType = commandType;
        this.periodStartTime = periodStartTime;
        this.periodEndTime = periodEndTime;
    }

    /**
     * Constructs an instance of a DisplayCommand, specifically for a 'find' command.
     */
    public DisplayCommand(CommandType commandType, String stringToSearch) {
        this.commandType = commandType;
        this.stringToSearch = stringToSearch;
    }

    /**
     * Processes the day operation accordingly.
     */
    private void processDay(TaskList taskList, Ui ui) throws InvictaException {
        ArrayList<Task> onDateTasks = taskList.getOnDateTasks(dateToSearch);
        ui.printFound(onDateTasks, dateToSearch);

    }

    /**
     * Processes the period operation accordingly.
     */
    private void processPeriod(TaskList taskList, Ui ui) throws InvictaException {
        ArrayList<Task> inPeriodTasks = taskList.getInPeriodTasks(periodStartTime, periodEndTime);
        ui.printFound(inPeriodTasks, periodStartTime, periodEndTime);
    }

    /**
     * Processes the find operation accordingly.
     */
    private void processFind(TaskList taskList, Ui ui) throws InvictaException {
        ArrayList<Task> matchingTasks = taskList.getMatchingTasks(stringToSearch);
        ui.printFound(matchingTasks, stringToSearch);
    }

    /**
     * Executes display-related command based on command type.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) throws InvictaException {
        switch (this.commandType) {
        case HELP: {
            ui.help();
            break;
        }
        case LIST: {
            if (taskList.isEmpty()) {
                ui.empty();
            } else {
                ui.printAll(taskList);
            }
            break;
        }
        case DAY: {
            if (taskList.isEmpty()) {
                ui.empty();
            } else {
                processDay(taskList, ui);
            }
            break;
        }
        case PERIOD: {
            if (taskList.isEmpty()) {
                ui.empty();
            } else {
                processPeriod(taskList, ui);
            }
            break;
        }
        case FIND: {
            if (taskList.isEmpty()) {
                ui.empty();
            } else {
                processFind(taskList, ui);
            }
            break;
        }
        default:
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_COMMAND));
        }
    }
}
