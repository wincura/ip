// CS2103T Individual Project by William Scott Win A0273291A

// Imports to use data structures
import java.time.LocalDate;
import java.util.ArrayList;

// Imports to handle files and user input
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Imports to handle time data
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;


public class InvictaBot {
    // data structures and file paths to be used by
    private static final String TASK_LIST_FILE_PATH = "./data/tasklist.txt";

    // More OOP Objects
    private Ui invictaUi;
    private Storage invictaStorage;
    private TaskList invictaTasks;

    public InvictaBot(String filePath) {
        try {
            invictaUi = new Ui();
            invictaStorage = new Storage(TASK_LIST_FILE_PATH);
            invictaTasks = new TaskList(invictaStorage.load());
        } catch (IOException e) {
            invictaUi.showLoadingError(e);
            invictaTasks = new TaskList(new ArrayList<>());
        } catch (InvictaException e) {
            invictaUi.showException(e);
            invictaTasks = new TaskList(new ArrayList<>());
        }
    }

    public void run() {
        // Loop to prompt user for username input until valid
        invictaUi.logo();
        System.out.println("_".repeat(100)
                + "\n\tHowdy! I'm InvictaBot!\n\tHow might I address you, pal?\n"
                + "_".repeat(100));
        String username;
        Scanner s = new Scanner(System.in);
        while (true) {
            try {
                // Obtaining user's name, with validation to handle empty names
                username = s.nextLine().trim();
                invictaUi.setUsername(username);
                if (username.isEmpty()) {
                    throw new InvictaException("\tSurely you're not a nameless person! Come again?");
                } else {
                    // Exit loop and continue to chatbot program
                    System.out.println("_".repeat(100)
                            + "\n\tIt's a pleasure, " + invictaUi.getUsername() + "! What can I do you for?\n"
                            + "_".repeat(100));
                    break;
                }
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }

        // Loop to keep chatbot running until bye prompt
        appLoop:
        while (true) {
            try {
                String raw = s.nextLine().trim();
                String[] userInput = raw.split(" ");
                if (raw.isEmpty()) {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tWhat? Did you say something? Type a message!\n"
                            + "_".repeat(100));
                } else {
                    Command c = Command.fromString(userInput[0]);
                    switch (c) {
                        case BYE: {
                            invictaUi.bye();
                            // Exit loop
                            break appLoop;
                        }
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
                            if (invictaTasks.getTaskList().isEmpty()) {
                                System.out.println("_".repeat(100)
                                        + "\n\tYour task list is empty! Add a few tasks!\n"
                                        + "_".repeat(100));
                            } else {
                                System.out.println("_".repeat(100)
                                        + "\n\tHere is a list of your tasks: ");
                                for (Task t : invictaTasks.getTaskList()) {
                                    number += 1;
                                    System.out.println("\t" + number + ". " + t.toString());
                                }
                                System.out.println("_".repeat(100));
                            }
                            break;
                        }
                        case DELETE: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: delete <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > invictaTasks.getTaskList().size() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? "
                                            + "Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task temp = invictaTasks.getTaskList().get(index);
                                    String deleteTask = temp.toString();
                                    invictaTasks.getTaskList().remove(index);
                                    invictaStorage.updateTaskListFile(invictaTasks);
                                    System.out.println("_".repeat(100)
                                            + "\n\tInto the trash! This task has been deleted: \n"
                                            + "\t\t" + deleteTask + "\n\tYou've got " + invictaTasks.getSize()
                                            + " tasks in your list now.\n"
                                            + "_".repeat(100));
                                }
                            }
                            break;
                        }
                        case MARK: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: mark <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > invictaTasks.getSize() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task t = invictaTasks.getTaskList().get(index);
                                    if (t.getDone()) {
                                        System.out.println("_".repeat(100)
                                                + "\n\tThis task is already marked as done: " + "\n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    } else {
                                        t.setDone(true);
                                        invictaStorage.updateTaskListFile(invictaTasks);
                                        System.out.println("_".repeat(100)
                                                + "\n\tGreat! I've marked this as done:  \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    }
                                }
                            }
                            break;
                        }
                        case UNMARK: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tPlease provide an index for this command. (usage: mark <number>)\n"
                                        + "_".repeat(100));
                            } else {
                                int index = Integer.parseInt(userInput[1]) - 1;
                                if (index < 0 | index > invictaTasks.getSize() - 1) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tYou want me to do what? Put a valid index! (check task list using 'list' command)\n"
                                            + "_".repeat(100));
                                } else {
                                    Task t = invictaTasks.getTaskList().get(index);
                                    if (!t.getDone()) {
                                        System.out.println("_".repeat(100)
                                                + "\n\tThis task is already marked as not done: \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    } else {
                                        t.setDone(false);
                                        invictaStorage.updateTaskListFile(invictaTasks);
                                        System.out.println("_".repeat(100)
                                                + "\n\tOh I see! I've marked this as not done: \n\t\t" + t + "\n"
                                                + "_".repeat(100));
                                    }
                                }
                            }
                            break;
                        }
                        case EVENT: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name, start time and end time! (usage: event <name> /from <start> /to <end>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                int taskNameLength = 0; // to be used later to pass user input words after task name
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    if (word.equals("/from")) {
                                        break;
                                    } else {
                                        taskName.append(word).append(" ");
                                        taskNameLength++;
                                    }
                                }
                                // pass remaining user input to extract period
                                String[] periodInput = Arrays.copyOfRange(userInput, taskNameLength + 1, userInput.length);
                                String[] period = Parser.handlePeriodInput(periodInput);
                                if (period[0].isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing start time and end time! (usage: event <name> /from <start> /to <end>)\n"
                                            + "_".repeat(100));
                                } else if (period[1].isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing end time! (usage: event <name> /from <start> /to <end>)" + "\n"
                                            + "_".repeat(100));
                                } else {
                                    LocalDateTime eventStartTime = Parser.handleDateTimeData(period[0].toString().trim());
                                    LocalDateTime eventEndTime = Parser.handleDateTimeData(period[1].toString().trim());
                                    Event ev = new Event(taskName.toString().trim(),
                                            eventStartTime,
                                            eventEndTime);
                                    invictaTasks.getTaskList().add(ev);
                                    invictaStorage.updateTaskListFile(invictaTasks);
                                    invictaUi.added(ev, invictaTasks);
                                }
                            }
                            break;
                        }
                        case DEADLINE: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name and deadline! (usage: deadline <name> /by <deadline>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                StringBuilder deadlineTimeString = new StringBuilder();
                                // Flags to mark where one argument ends and another begins, and when to disregard unnecessary arguments
                                boolean taskNameDone = false;
                                int argsDoneFlag = 1;
                                // Start counting from index 1 to ignore deadline command
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    if (word.equals("/by")) {
                                        taskNameDone = true;
                                        argsDoneFlag -= 1;
                                        if (argsDoneFlag < 0) {
                                            break;
                                        }
                                    } else if (taskNameDone) {
                                        deadlineTimeString.append(word).append(" ");
                                    } else {
                                        taskName.append(word).append(" ");
                                    }
                                }
                                if (deadlineTimeString.isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing deadline! (usage: deadline <name> /by <deadline>)\n"
                                            + "_".repeat(100));
                                } else {
                                    LocalDateTime deadlineTime = Parser.handleDateTimeData(deadlineTimeString.toString().trim());
                                    Deadline dl = new Deadline(taskName.toString().trim(),
                                            deadlineTime);
                                    invictaTasks.getTaskList().add(dl);
                                    invictaStorage.updateTaskListFile(invictaTasks);
                                    invictaUi.added(dl, invictaTasks);
                                }
                            }
                            break;
                        }
                        case TODO: {
                            if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing task name! (usage: todo <name>)\n"
                                        + "_".repeat(100));
                            } else {
                                StringBuilder taskName = new StringBuilder();
                                // Start counting from index 1 to ignore todo command
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    taskName.append(word).append(" ");
                                }
                                Todo td = new Todo(taskName.toString().trim());
                                invictaTasks.getTaskList().add(td);
                                invictaStorage.updateTaskListFile(invictaTasks);
                                invictaUi.added(td, invictaTasks);
                            }
                            break;
                        }
                        case DAY: {
                            LocalDate dateToSearch;
                            StringBuilder dateToSearchString = new StringBuilder();
                            if (invictaTasks.getTaskList().isEmpty()) {
                                System.out.println("_".repeat(100)
                                        + "\n\tYour task list is empty! Add a few tasks!\n"
                                        + "_".repeat(100));

                            } else if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing date! (usage: day <date>)\n"
                                        + "_".repeat(100));
                            } else {
                                for (int i = 1; i < userInput.length; i++) {
                                    String word = userInput[i];
                                    dateToSearchString.append(word).append(" ");
                                }
                            }
                            dateToSearch = Parser.handleDateTimeData(dateToSearchString
                                    .toString().trim()).toLocalDate(); // time values are disregarded

                            ArrayList<Task> onDateTasks = new ArrayList<>();
                            // add the tasks to temp ArrayList of Tasks to be displayed
                            for (Task t : invictaTasks.getTaskList()) {
                                if (t instanceof Deadline) {
                                    if (((Deadline) t).getDeadline().toLocalDate().isEqual(dateToSearch)) {
                                        onDateTasks.add(t);
                                    }
                                }
                                if (t instanceof Event) {
                                    // using inclusive checks ensures not missing events that fall on the start and end dates
                                    if ((((Event) t).getStart().toLocalDate().isEqual(dateToSearch)
                                            || ((Event) t).getStart().toLocalDate().isBefore(dateToSearch))
                                            && (((Event) t).getEnd().toLocalDate().isEqual(dateToSearch)
                                            || ((Event) t).getEnd().toLocalDate().isAfter(dateToSearch))) {
                                        onDateTasks.add(t);
                                    }
                                }
                            }
                            int number = 0;
                            if (onDateTasks.isEmpty()) {
                                System.out.println("_".repeat(100)
                                        + "\n\tThere are no events on that day. Wanna add a task?\n"
                                        + "_".repeat(100));
                            } else {
                                System.out.println("_".repeat(100)
                                        + "\n\tHere is a list of your tasks that you have on "
                                        + dateToSearch.format(Parser.dateDisplay) + ": ");
                                for (Task t : onDateTasks) {
                                    number += 1;
                                    System.out.println("\t" + number + ". " + t.toString());
                                }
                                System.out.println("_".repeat(100));
                            }
                            break;
                        }
                        case PERIOD: {
                            if (invictaTasks.getTaskList().isEmpty()) {
                                System.out.println("_".repeat(100)
                                        + "\n\tYour task list is empty! Add a few tasks!\n"
                                        + "_".repeat(100));

                            } else if (userInput.length < 2) {
                                throw new InvictaException("_".repeat(100)
                                        + "\n\tMissing start time and end time! (usage: period /from <start> /to <end>)\n"
                                        + "_".repeat(100));
                            } else {
                                String[] periodInput = Arrays.copyOfRange(userInput,1, userInput.length);
                                String[] period = Parser.handlePeriodInput(periodInput);
                                if (period[0].isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing start time and end time! (usage: period /from <start> /to <end>)\n"
                                            + "_".repeat(100));
                                } else if (period[1].isEmpty()) {
                                    throw new InvictaException("_".repeat(100)
                                            + "\n\tMissing end time! (usage: period /from <start> /to <end>)" + "\n"
                                            + "_".repeat(100));
                                } else {
                                    LocalDateTime periodStartTime = Parser.handleDateTimeData(period[0].trim());
                                    LocalDateTime periodEndTime = Parser.handleDateTimeData(period[1].trim());
                                    ArrayList<Task> inPeriodTasks = new ArrayList<>();
                                    // add the tasks to temp ArrayList of Tasks to be displayed
                                    for (Task t : invictaTasks.getTaskList()) {
                                        if (t instanceof Deadline) {
                                            if ((((Deadline) t).getDeadline().isEqual(periodStartTime)
                                                    || ((Deadline) t).getDeadline().isAfter(periodStartTime))
                                                    && ((((Deadline) t).getDeadline().isEqual(periodEndTime)
                                                    || ((Deadline) t).getDeadline().isBefore(periodEndTime)))) {
                                                inPeriodTasks.add(t);
                                            }
                                        }
                                        if (t instanceof Event) {
                                            // using inclusive checks and start time to check ensures not missing events that extend beyond period
                                            if ((((Event) t).getStart().isEqual(periodStartTime) || ((Event) t).getEnd().isEqual(periodStartTime))
                                                    || (((Event) t).getStart().isEqual(periodEndTime) || ((Event) t).getEnd().isEqual(periodEndTime))
                                                    || (((Event) t).getEnd().isAfter(periodStartTime)) && ((Event) t).getStart().isBefore(periodStartTime)
                                                    || (((Event) t).getStart().isAfter(periodStartTime)) && ((Event) t).getEnd().isBefore(periodEndTime)
                                                    || (((Event) t).getStart().isBefore(periodEndTime)) && ((Event) t).getEnd().isAfter(periodEndTime)
                                                    || (((Event) t).getStart().isBefore(periodStartTime)) && ((Event) t).getEnd().isAfter(periodEndTime)) {
                                                inPeriodTasks.add(t);
                                            }
                                        }
                                    }
                                    int number = 0;
                                    System.out.println("_".repeat(100)
                                            + "\n\tHere is a list of your tasks that fall within "
                                            + periodStartTime.format(Parser.dateDisplay) + " to " + periodEndTime.format(Parser.dateDisplay) + ": ");
                                    for (Task t : inPeriodTasks) {
                                        number += 1;
                                        System.out.println("\t" + number + ". " + t.toString());
                                    }
                                    System.out.println("_".repeat(100));
                                    break;
                                }
                            }
                            break;
                        }
                        default: {
                            invictaUi.bye();;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("_".repeat(100)
                        + "\n\tAn index is a number, so go put one! (usage: mark/unmark <int as index>)\n"
                        + "_".repeat(100));
            } catch (DateTimeParseException e) {
                System.out.println("_".repeat(100)
                        + "\n\tInvalid date time format! Type 'help' to view acceptable formats.\n"
                        + "_".repeat(100));
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new InvictaBot(TASK_LIST_FILE_PATH).run();

    }
}
