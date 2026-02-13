package invicta.app;

// Imports to handle data structure operations
import java.util.Arrays;

// Imports to handle time data
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Imports to return and use command and task classes after parsing
import invicta.app.Ui;
import invicta.command.AddCommand;
import invicta.command.Command;
import invicta.command.CommandType;
import invicta.command.DisplayCommand;
import invicta.command.EditCommand;
import invicta.command.ExitCommand;
import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Todo;
import invicta.app.Message.MessageKey;


/**
 * Handles parsing of user input to return commands or date time data
 */
public class Parser {
    // date time formats and formatters to be used by the chatbot
    public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    public static final String FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_DISPLAY = "MMM dd yyyy (EEE)";
    public static DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern(FORMAT_DATE_ONLY);
    public static DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern(FORMAT_DATE_AND_TIME);
    public static DateTimeFormatter dateDisplay = DateTimeFormatter.ofPattern(FORMAT_DATE_DISPLAY);


    public static void processUsername(Scanner s, String username, Ui ui) {
        while (true) {
            try {
                // Obtaining user's name, with validation to handle empty names
                username = s.nextLine().trim();
                ui.setUsername(username);
                if (username.isEmpty()) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_USERNAME));
                } else {
                    break;
                }
            } catch (InvictaException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Returns a Command object based on command input String array provided.
     * The subtype of Command object depends on the command input.
     *
     * @param commandString String to be parsed into Command Object.
     * @param commandType Type of command for which command data is parsed.
     * @return Command object with respective parameters based on command input string.
     * @throws InvictaException when command input is of invalid format.
     */
    public static Command parseCommandData(String[] commandString, CommandType commandType) throws InvictaException {
        if (commandString.length == 0) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_INPUT));
        } else {
            switch (commandType) {
            case BYE: {
                return new ExitCommand();
            }
            case LIST, HELP: {
                return new DisplayCommand(commandType);
            }
            case DELETE: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_INDEX,
                            Message.getUsageMessage(MessageKey.DELETE_USAGE)));
                } else {
                    int index = Integer.parseInt(commandString[1]) - 1;
                    return new EditCommand(commandType, index);
                }
            }
            case MARK: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_INDEX,
                            Message.getUsageMessage(MessageKey.MARK_USAGE)));
                } else {
                    int index = Integer.parseInt(commandString[1]) - 1;
                    return new EditCommand(commandType, index);
                }
            }
            case UNMARK: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_INDEX,
                            Message.getUsageMessage(MessageKey.UNMARK_USAGE)));
                } else {
                    int index = Integer.parseInt(commandString[1]) - 1;
                    return new EditCommand(commandType, index);
                }
            }
            case EVENT: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(
                            new MessageKey[] {MessageKey.MISSING_NAME,
                                    MessageKey.MISSING_EVENT_START, MessageKey.MISSING_EVENT_END},
                            Message.getUsageMessage(MessageKey.EVENT_USAGE)));
                } else {
                    StringBuilder taskName = new StringBuilder();
                    int taskNameLength = 0; // to be used later to pass user input words after task name
                    for (int i = 1; i < commandString.length; i++) {
                        String word = commandString[i];
                        if (word.equals("/from")) {
                            break;
                        } else {
                            taskName.append(word).append(" ");
                            taskNameLength++;
                        }
                    }
                    // pass remaining user input to extract period
                    String[] periodInput = Arrays.copyOfRange(commandString, taskNameLength + 1, commandString.length);
                    String[] period = Parser.parsePeriodData(periodInput);
                    if (period[0].isEmpty()) {
                        throw new InvictaException(Message.getChatbotMessage(
                                new MessageKey[] {MessageKey.MISSING_EVENT_START, MessageKey.MISSING_EVENT_END},
                                Message.getUsageMessage(MessageKey.EVENT_USAGE)));
                    } else if (period[1].isEmpty()) {
                        throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_EVENT_END,
                                Message.getUsageMessage(MessageKey.EVENT_USAGE)));
                    } else {
                        LocalDateTime eventStartTime = Parser.parseDateTimeData(period[0].toString().trim());
                        LocalDateTime eventEndTime = Parser.parseDateTimeData(period[1].toString().trim());
                        Event ev = new Event(taskName.toString().trim(),
                                eventStartTime,
                                eventEndTime);
                        return new AddCommand(ev);
                    }
                }
            }
            case DEADLINE: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(
                            new MessageKey[] {MessageKey.MISSING_NAME, MessageKey.MISSING_DEADLINE},
                            Message.getUsageMessage(MessageKey.DEADLINE_USAGE)));
                } else {
                    StringBuilder taskName = new StringBuilder();
                    StringBuilder deadlineTimeString = new StringBuilder();
                    // Flags to mark where one argument ends and another begins, and when to disregard unnecessary arguments
                    boolean taskNameDone = false;
                    int argsDoneFlag = 1;
                    // Start counting from index 1 to ignore deadline command
                    for (int i = 1; i < commandString.length; i++) {
                        String word = commandString[i];
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
                        throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_DEADLINE,
                                Message.getUsageMessage(MessageKey.DEADLINE_USAGE)));
                    } else {
                        LocalDateTime deadlineTime = Parser.parseDateTimeData(deadlineTimeString.toString().trim());
                        Deadline dl = new Deadline(taskName.toString().trim(),
                                deadlineTime);
                        return new AddCommand(dl);
                    }
                }
            }
            case TODO: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_NAME,
                            Message.getUsageMessage(MessageKey.TODO_USAGE)));
                } else {
                    StringBuilder taskName = new StringBuilder();
                    // Start counting from index 1 to ignore todo command
                    for (int i = 1; i < commandString.length; i++) {
                        String word = commandString[i];
                        taskName.append(word).append(" ");
                    }
                    Todo td = new Todo(taskName.toString().trim());
                    return new AddCommand(td);
                }
            }
            case FIND: {
                String stringToSearch;
                StringBuilder stringToSearchString = new StringBuilder();
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_STRING,
                            Message.getUsageMessage(MessageKey.FIND_USAGE)));
                } else {
                    for (int i = 1; i < commandString.length; i++) {
                        String word = commandString[i];
                        stringToSearchString.append(word).append(" ");
                    }
                    stringToSearch = stringToSearchString.toString().trim();
                    return new DisplayCommand(commandType, stringToSearch);
                }
            }
            case DAY: {
                LocalDate dateToSearch;
                StringBuilder dateToSearchString = new StringBuilder();
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_DAY,
                            Message.getUsageMessage(MessageKey.DAY_USAGE)));
                } else {
                    for (int i = 1; i < commandString.length; i++) {
                        String word = commandString[i];
                        dateToSearchString.append(word).append(" ");
                    }
                    dateToSearch = Parser.parseDateTimeData(dateToSearchString
                            .toString().trim()).toLocalDate(); // time values are disregarded
                    return new DisplayCommand(commandType, dateToSearch);
                }
            }
            case PERIOD: {
                if (commandString.length < 2) {
                    throw new InvictaException(Message.getChatbotMessage(
                            new MessageKey[] {MessageKey.MISSING_PERIOD_START, MessageKey.MISSING_PERIOD_END},
                            Message.getUsageMessage(MessageKey.PERIOD_USAGE)));
                } else {
                    String[] periodInput = Arrays.copyOfRange(commandString,1, commandString.length);
                    String[] period = Parser.parsePeriodData(periodInput);
                    if (period[0].isEmpty()) {
                        throw new InvictaException(Message.getChatbotMessage(
                                new MessageKey[] {MessageKey.MISSING_PERIOD_START, MessageKey.MISSING_PERIOD_END},
                                Message.getUsageMessage(MessageKey.PERIOD_USAGE)));
                    } else if (period[1].isEmpty()) {
                        throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_PERIOD_END,
                                Message.getUsageMessage(MessageKey.PERIOD_USAGE)));
                    }
                    LocalDateTime periodStartTime = Parser.parseDateTimeData(period[0].trim());
                    LocalDateTime periodEndTime = Parser.parseDateTimeData(period[1].trim());
                    return new DisplayCommand(commandType, periodStartTime, periodEndTime);
                }
            }
            default: {
                throw new InvictaException(Message.getChatbotMessage(Message.MessageKey.INVALID_COMMAND,
                        Message.getUsageMessage(Message.MessageKey.TYPE_HELP)));
            }
            }
        }
    }

    /**
     * Returns a LocalDateTime object based on String provided.
     * If only date is provided, the time is set to midnight.
     *
     * @param dateTimeString String to be parsed into LocalDateTime object.
     * @return dateTime LocalDateTime object based on format in input string.
     * @throws DateTimeParseException when string is of invalid format.
     */
    public static LocalDateTime parseDateTimeData(String dateTimeString) {
        if (dateTimeString.length() <= 10) {
            return LocalDate.parse(dateTimeString, dateOnly).atStartOfDay();
        } else {
            return LocalDateTime.parse(dateTimeString, dateAndTime);
        }
    }

    /**
     * Returns the start and end times of a period in a string array by
     * iterating through user input to extract strings representing start and end times of a period.
     * If more user arguments in input than expected, they are disregarded.
     *
     * @param userInput String array to be parsed into period start and end times.
     * @return period String array containing the strings representing start and end times of a period.
     */
    public static String[] parsePeriodData(String[] userInput) {
        String[] period = new String[2];
        StringBuilder periodStartTimeString = new StringBuilder();
        StringBuilder periodEndTimeString = new StringBuilder();
        // Flags to mark where one argument ends and another begins, and when to disregard unnecessary arguments
        boolean eventStartDone = false;
        int argsDoneFlag = 2;
        // Start counting from index 1 to ignore event command
        for (int i = 1; i < userInput.length; i++) {
            String word = userInput[i];
            if (word.equals("/from")) {
                argsDoneFlag -= 1;
                if (argsDoneFlag < 1) {
                    break;
                }
            } else if (word.equals("/to")) {
                eventStartDone = true;
                argsDoneFlag -= 1;
                if (argsDoneFlag < 1) {
                    break;
                }
            } else if (eventStartDone) {
                periodEndTimeString.append(word).append(" ");
            } else {
                periodStartTimeString.append(word).append(" ");
            }
        }
        period[0] = periodStartTimeString.toString();
        period[1] = periodEndTimeString.toString();
        return period;
    }

}
