package invicta.command;

// Imports to use data structures
import java.util.ArrayList;

// Imports to handle time data
import java.time.LocalDateTime;
import java.time.LocalDate;

// Imports to use invicta app components and task list to execute operations
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

    public DisplayCommand(CommandType commandType) {
        this.commandType = commandType;
    }

    public DisplayCommand(CommandType commandType, LocalDate dateToSearch) {
        this.commandType = commandType;
        this.dateToSearch = dateToSearch;
    }

    public DisplayCommand(CommandType commandType, LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        this.commandType = commandType;
        this.periodStartTime = periodStartTime;
        this.periodEndTime = periodEndTime;
    }

    public DisplayCommand(CommandType commandType, String stringToSearch) {
        this.commandType = commandType;
        this.stringToSearch = stringToSearch;
    }

    /**
     * Processes the day operation accordingly.
     */
    private void processDay(TaskList taskList, Ui ui) {
        ArrayList<Task> onDateTasks = taskList.getOnDateTasks(dateToSearch);

        int number = 0;
        if (onDateTasks.isEmpty()) {
            System.out.println(Ui.SEPARATOR
                    + "\n\tThere are no events on that day. Wanna add a task?\n"
                    + Ui.SEPARATOR);
        } else {
            ui.printFound(onDateTasks, dateToSearch);
        }
    }

    /**
     * Processes the period operation accordingly.
     */
    private void processPeriod(TaskList taskList, Ui ui) {
        ArrayList<Task> inPeriodTasks = taskList.getInPeriodTasks(periodStartTime, periodEndTime);

        int number = 0;
        if (inPeriodTasks.isEmpty()) {
            System.out.println(Ui.SEPARATOR
                    + "\n\tThere are no events on that day. Wanna add a task?\n"
                    + Ui.SEPARATOR);
        } else {
            ui.printFound(inPeriodTasks, periodStartTime, periodEndTime);
        }
    }

    /**
     * Processes the find operation accordingly.
     */
    private void processFind(TaskList taskList, Ui ui) {
        ArrayList<Task> matchingTasks = taskList.getMatchingTasks(stringToSearch);
        if (matchingTasks.isEmpty()) {
            System.out.println(Ui.SEPARATOR
                    + "\n\tThere are no matching tasks. Wanna add a task?\n"
                    + Ui.SEPARATOR);
        } else {
            ui.printFound(matchingTasks, stringToSearch);
        }
    }

    /**
     * Executes display-related command based on command type.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) {
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

        }
    }
}
