package invicta.command;

import invicta.app.InvictaException;
import invicta.app.Storage;
import invicta.app.Ui;
import invicta.task.Task;
import invicta.task.TaskList;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

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
     * Executes display-related command based on command type.
     *
     * @param taskList TaskList object that handles task list operations.
     * @param storage Storage object that handles loading and updating of files.
     * @param ui Ui object that handles user input and displaying.
     */
    public void execute(TaskList taskList, Storage storage, Ui ui) {
        switch (this.commandType) {
            case HELP: {
                System.out.println("_".repeat(100)
                        + "\n\tList of commands in InvictaBot:\n"
                        + "\tbye - exit app\n"
                        + "\tlist - display task list\n"
                        + "\tdelete - delete the task\n"
                        + "\tmark <index> - mark task as done\n"
                        + "\tunmark <index> - mask task as not done\n"
                        + "\ttodo <name> - add a to-do task\n"
                        + "\tdeadline <name> /by <deadline> - add a deadline task\n"
                        + "\tevent <name> /from <start> /to <end> - add an event\n"
                        + "\tfind <search string> - display tasks containing search string in task descriptions\n"
                        + "\tday <end> - display tasks on date\n"
                        + "\tperiod /from <start> /to <end> - display tasks within period\n\n"
                        + "\tList of available date time formats:\n"
                        + "\tyyyy-MM-dd\n"
                        + "\tyyyy-MM-dd HH:mm\n"
                        + "_".repeat(100));
                break;
            }
            case LIST: {
                int number = 0;
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

                    ArrayList<Task> onDateTasks = taskList.getOnDateTasks(dateToSearch);

                    int number = 0;
                    if (onDateTasks.isEmpty()) {
                        System.out.println("_".repeat(100)
                                + "\n\tThere are no events on that day. Wanna add a task?\n"
                                + "_".repeat(100));
                    } else {
                        ui.printFound(onDateTasks, dateToSearch);
                    }
                }
                break;
            }
            case PERIOD: {
                if (taskList.isEmpty()) {
                    ui.empty();
                } else {

                    ArrayList<Task> inPeriodTasks = taskList.getInPeriodTasks(periodStartTime, periodEndTime);

                    int number = 0;
                    if (inPeriodTasks.isEmpty()) {
                        System.out.println("_".repeat(100)
                                + "\n\tThere are no events on that day. Wanna add a task?\n"
                                + "_".repeat(100));
                    } else {
                        ui.printFound(inPeriodTasks, periodStartTime, periodEndTime);
                    }
                }
                break;
            }
        case FIND: {
            if (taskList.isEmpty()) {
                ui.empty();
            } else {
                ArrayList<Task> matchingTasks = taskList.getMatchingTasks(stringToSearch);
                if (matchingTasks.isEmpty()) {
                    System.out.println("_".repeat(100)
                            + "\n\tThere are no matching tasks. Wanna add a task?\n"
                            + "_".repeat(100));
                } else {
                    ui.printFound(matchingTasks, stringToSearch);
                }
            }
            break;
        }

        }

    }
}
