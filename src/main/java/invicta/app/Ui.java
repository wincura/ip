package invicta.app;

// Imports to handle user input, ime data and to use data structures
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Imports to use task and task list data for display
import invicta.task.Task;
import invicta.task.TaskList;


/**
 * Handles user interactions with InvictaBot.
 * Orchestrates the display of chatbot messages represented by the Message class.
 */
public class Ui {

    private String username;

    public Ui() {
        this.username = "";
    }

    public Ui(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Displays InvictaBot logo.
     */
    public void logo() {
        // Logo below generated with the help of an external tool from https://patorjk.com/software/taag/
        String logo = """
                .___            .__        __        __________        __
                |   | _______  _|__| _____/  |______ \\______   \\ _____/  |_
                |   |/    \\  \\/ /  |/ ___\\   __\\__  \\ |    |  _//  _ \\   __\\
                |   |   |  \\   /|  \\  \\___|  |  / __ \\|    |   (  <_> )  |
                |___|___|  /\\_/ |__|\\___  >__| (____  /______  /\\____/|__|
                         \\/             \\/          \\/       \\/""";
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.HELLO, logo));
    }

    /**
     * Displays help message.
     */
    public void help() {
        System.out.println(Message.getChatbotMessage(MessageKey.DISPLAY_HELP));
    }

    /**
     * Displays goodbye message with username.
     */
    public void bye() {
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.BYE, this.username));
    }

    /**
     * Displays message when adding tasks, including count.
     *
     * @param t Task whose details are to be printed.
     * @param taskList Task list whose count are to be printed.
     */
    public void added(Task t, TaskList taskList) {
        String size = Integer.toString(taskList.getSize());
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.ADDED_TASK, t.toString(), size));
    }

    /**
     * Displays message when deleting tasks, including count.
     *
     * @param t Task whose details are to be printed.
     * @param taskList Task list whose count are to be printed.
     */
    public void deleted(Task t, TaskList taskList) {
        String size = Integer.toString(taskList.getSize());
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.DELETED_TASK, t.toString(), size));
    }

    /**
     * Displays the result after attempting to mark/unmark a task as done,
     * depending on whether it is already marked/unmarked, as well as details of the task.
     *
     * @param i Option indicating already marked/unmarked scenarios
     * @param task Task whose details are to be printed.
     */
    public void marked(int i, String task) throws InvictaException {
        switch (i) {
        case 1: {
            System.out.println(Message.getChatbotMessageFormatted(MessageKey.MARKED_DONE, task));
            break;
        }
        case 2: {
            System.out.println(Message.getChatbotMessageFormatted(MessageKey.MARKED_DONE_ALREADY, task));
            break;
        }
        case 3: {
            System.out.println(Message.getChatbotMessageFormatted(MessageKey.MARKED_NOT_DONE, task));
            break;
        }
        case 4: {
            System.out.println(Message.getChatbotMessageFormatted(MessageKey.MARKED_NOT_DONE_ALREADY, task));
            break;
        }
        default:
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_COMMAND));
        }
    }

    /**
     * Displays message indicating an empty task list.
     */
    public void empty() {
        System.out.println(Message.getChatbotMessage(MessageKey.EMPTY_TASK_LIST));
    }

    /**
     * Displays all the tasks in task list, along with their details.
     *
     * @param taskList Provided task list to print out.
     */
    public void printAll(TaskList taskList) {
        int number = 0;
        String listMessage = Message.buildListMessage(taskList.getTaskList());
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.DISPLAY_TASK_LIST, listMessage));
    }

    /**
     * Displays the tasks that fall on a specified date, along with their details.
     *
     * @param taskList Provided list of found tasks to be printed.
     * @param dateToSearch Date used to find the tasks to be printed.
     */
    public void printFound(ArrayList<Task> taskList, LocalDate dateToSearch) throws InvictaException {
        if (taskList.isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.EMPTY_DAY,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        }
        String listMessage = Message.buildListMessage(taskList);
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.DISPLAY_TASK_LIST_DAY,
                dateToSearch.format(Parser.dateDisplay), listMessage));
    }

    /**
     * Displays the tasks that fall within a specified period, along with their details.
     *
     * @param taskList Provided list of found tasks to be printed.
     * @param periodStartTime Period start time used to find the tasks to be printed.
     * @param periodEndTime Period end time used to find the tasks to be printed.
     */
    public void printFound(ArrayList<Task> taskList, LocalDateTime periodStartTime, LocalDateTime periodEndTime)
            throws InvictaException {
        if (taskList.isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.EMPTY_PERIOD,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        }
        String listMessage = Message.buildListMessage(taskList);
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.DISPLAY_TASK_LIST_PERIOD,
                periodStartTime.format(Parser.dateDisplay), periodEndTime.format(Parser.dateDisplay), listMessage));
    }

    /**
     * Displays the tasks that contains search string in their description.
     *
     * @param taskList Provided list of found tasks to be printed.
     * @param stringToSearch String used to find the tasks to be printed.
     */
    public void printFound(ArrayList<Task> taskList, String stringToSearch) throws InvictaException {
        if (taskList.isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.EMPTY_FIND,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        }
        String listMessage = Message.buildListMessage(taskList);
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.DISPLAY_TASK_LIST_FIND,
                stringToSearch, listMessage));
    }

    /**
     * Reads user input for language until valid input.
     *
     * @param s Scanner used to read user input for language.
     */
    public void readLanguage(Scanner s) {
        System.out.println(Message.getChatbotMessage(MessageKey.PROMPT_LANGUAGE));
        Parser.processLanguage(s);
    }

    /**
     * Reads user input for username and loop until valid input.
     *
     * @param s Scanner used to read user input for username.
     */
    public void readUsername(Scanner s) {
        System.out.println(Message.getChatbotMessage(MessageKey.PROMPT_USERNAME));
        String username = "";
        Parser.processUsername(s, username, this);
        System.out.println(Message.getChatbotMessageFormatted(MessageKey.PROMPT_COMMAND, this.getUsername()));
    }

    /**
     * Reads user input from scanner into a string array.
     *
     * @param s scanner object with raw user input.
     * @return userInput string array containing user input split by spaces.
     */
    public String[] readCommand(Scanner s) throws InvictaException {
        String raw = s.nextLine().trim();
        String[] userInput = raw.split(" ");
        if (raw.isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_INPUT));
        } else {
            return userInput;
        }
    }

    /**
     * Displays error message for taskbot specific errors occurring during operations.
     * Prints using defined error message.
     */
    public void showException(Exception e) {
        if (e instanceof NumberFormatException) {
            System.out.println(Message.getChatbotMessage(MessageKey.INVALID_INDEX,
                    Message.getUsageMessage(MessageKey.LIST_USAGE)));
        } else if (e instanceof DateTimeParseException) {
            System.out.println(Message.getChatbotMessage(MessageKey.INVALID_DATE_TIME,
                    Message.getUsageMessage(MessageKey.TYPE_HELP)));
        } else if (e instanceof IOException) {
            System.out.println(Message.getIoMessage(MessageKey.FILE_IO_ERROR, e.getMessage()));
        } else {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays messages related to reading and writing from file.
     */
    public static void showIoMessages(MessageKey key, String details) {
        String ioMessage = Message.getIoMessage(key);
        System.out.println(ioMessage + " " + details);
    }
}

