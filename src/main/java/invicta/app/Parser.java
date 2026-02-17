package invicta.app;

// Imports to handle time data and handle data structure operations
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

// Imports to return and use command and task classes after parsing
import invicta.command.AddCommand;
import invicta.command.Command;
import invicta.command.CommandType;
import invicta.command.DisplayCommand;
import invicta.command.EditCommand;
import invicta.command.ExitCommand;
import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Todo;

/**
 * Handles parsing of user input to return commands or date time data
 */
public class Parser {
    // date time formats and formatters to be used by the chatbot
    public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    public static final String FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_DISPLAY = "MMM dd yyyy (EEE)";
    protected static DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern(FORMAT_DATE_ONLY);
    protected static DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern(FORMAT_DATE_AND_TIME);
    protected static DateTimeFormatter dateDisplay = DateTimeFormatter.ofPattern(FORMAT_DATE_DISPLAY);

    /**
     * Checks user input tokens and throw exception if minimum arguments requirement of 2 is not met.
     *
     * @param userInput  String array to check user input in.
     * @param missingKey Respective error message for the input field.
     * @param usageKey   Respective usage message for the command.
     * @throws InvictaException if minimum number of arguments not present.
     */
    private static void requireMinArgs(String[] userInput, MessageKey missingKey, MessageKey usageKey)
            throws InvictaException {
        if (userInput.length < 2) {
            throw new InvictaException(
                    Message.getChatbotMessage(
                            missingKey,
                            Message.getUsageMessage(usageKey)));
        }
    }

    /**
     * Checks user input and throw exception if empty.
     *
     * @param input String to check.
     * @param missingKey Respective error message for the input field.
     * @throws InvictaException when input is empty.
     */
    private static void requireInput(String input, MessageKey missingKey)
            throws InvictaException {
        if (input.isEmpty()) {
            throw new InvictaException(
                    Message.getChatbotMessage(missingKey));
        }
    }

    /**
     * Processes the user input for language selection.
     */
    public static void processLanguage(Scanner s, Ui ui) throws InvictaException {
        String langChoice = s.nextLine().trim();
        requireInput(langChoice, MessageKey.MISSING_LANGUAGE);
        assert !(langChoice.isEmpty()) : "Data for language missing!";
        switch (langChoice) {
        case "en":
            Message.setLang(Message.Lang.EN);
            break;
        case "fr":
            Message.setLang(Message.Lang.FR);
            break;
        case "es":
            Message.setLang(Message.Lang.ES);
            break;
        default:
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_LANGUAGE,
                    Message.getUsageMessage(MessageKey.LANGUAGE_USAGE)));
        }
    }

    /**
     * Processes the user input for username.
     */
    public static void processUsername(Scanner s, Ui ui) throws InvictaException {
        String username = s.nextLine().trim();
        requireInput(username, MessageKey.MISSING_USERNAME);
        assert !(username.isEmpty()) : "Data for username missing!";
        ui.setUsername(username);
    }

    /**
     * Returns an EditCommand with respective usage message for invalid input.
     */
    public static EditCommand processEditCommand(String[] commandString,
                                          CommandType commandType, MessageKey usage) throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_INDEX, usage);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
        int index = Integer.parseInt(commandString[1]) - 1;
        return new EditCommand(commandType, index);
    }

    /**
     * Returns an AddCommand to add event task with respective start and end times.
     */
    public static AddCommand processEventCommand(String[] commandString) throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_NAME, MessageKey.EVENT_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
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
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_EVENT_START,
                    Message.getUsageMessage(MessageKey.EVENT_USAGE)));
        } else if (period[1].isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_EVENT_END,
                    Message.getUsageMessage(MessageKey.EVENT_USAGE)));
        }
        LocalDateTime eventStartTime = Parser.parseDateTimeData(period[0].trim());
        LocalDateTime eventEndTime = Parser.parseDateTimeData(period[1].trim());
        if (eventEndTime.isBefore(eventStartTime)) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_PERIOD));
        }
        Event ev = new Event(taskName.toString().trim(),
                eventStartTime,
                eventEndTime);
        return new AddCommand(ev);
    }

    /**
     * Returns an AddCommand to add deadline task with respective deadline time.
     */
    public static AddCommand processDeadlineCommand(String[] commandString) throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_NAME, MessageKey.DEADLINE_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
        StringBuilder taskName = new StringBuilder();
        StringBuilder deadlineTimeString = new StringBuilder();
        // Flags to mark where one argument ends and another begins,
        // and when to disregard unnecessary arguments
        boolean isEndOfTaskName = false;
        int argsDoneFlag = 1;
        // Start counting from index 1 to ignore deadline command
        for (int i = 1; i < commandString.length; i++) {
            String word = commandString[i];
            if (word.equals("/by")) {
                isEndOfTaskName = true;
                argsDoneFlag -= 1;
                if (argsDoneFlag < 0) {
                    break;
                }
            } else if (isEndOfTaskName) {
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

    /**
     * Returns an AddCommand to add a todo task.
     */
    public static AddCommand processTodoCommand(String[] commandString) throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_NAME, MessageKey.TODO_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
        StringBuilder taskName = new StringBuilder();
        // Start counting from index 1 to ignore todo command
        for (int i = 1; i < commandString.length; i++) {
            String word = commandString[i];
            taskName.append(word).append(" ");
        }
        Todo td = new Todo(taskName.toString().trim());
        return new AddCommand(td);
    }

    /**
     * Returns an DisplayCommand to find tasks with matching string.
     */
    public static DisplayCommand processFindCommand(String[] commandString, CommandType commandType)
            throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_STRING, MessageKey.FIND_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
        String stringToSearch;
        StringBuilder stringToSearchString = new StringBuilder();
        for (int i = 1; i < commandString.length; i++) {
            String word = commandString[i];
            stringToSearchString.append(word).append(" ");
        }
        stringToSearch = stringToSearchString.toString().trim();
        return new DisplayCommand(commandType, stringToSearch);
    }

    /**
     * Returns an DisplayCommand to find tasks that fall on given date.
     */
    public static DisplayCommand processDayCommand(String[] commandString, CommandType commandType)
            throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_STRING, MessageKey.FIND_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
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


    /**
     * Returns an DisplayCommand to find tasks with fall within given period.
     */
    public static DisplayCommand processPeriodCommand(String[] commandString, CommandType commandType)
            throws InvictaException {
        requireMinArgs(commandString, MessageKey.MISSING_PERIOD_START, MessageKey.PERIOD_USAGE);
        assert !(commandString.length < 2) : "Minimum size of arguments not met!";
        String[] periodInput = Arrays.copyOfRange(commandString, 1, commandString.length);
        String[] period = Parser.parsePeriodData(periodInput);
        if (period[0].isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_PERIOD_START,
                    Message.getUsageMessage(MessageKey.PERIOD_USAGE)));
        } else if (period[1].isEmpty()) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.MISSING_PERIOD_END,
                    Message.getUsageMessage(MessageKey.PERIOD_USAGE)));
        }
        LocalDateTime periodStartTime = Parser.parseDateTimeData(period[0].trim());
        LocalDateTime periodEndTime = Parser.parseDateTimeData(period[1].trim());
        if (periodEndTime.isBefore(periodStartTime)) {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_PERIOD));
        }
        return new DisplayCommand(commandType, periodStartTime, periodEndTime);
    }

    /**
     * Returns a Command object based on command input  provided.
     * The subtype of Command object depends on the command input.
     *
     * @param raw String to parse input from.
     * @return Command object with respective parameters based on command input string.
     * @throws InvictaException when command input is of invalid format.
     */
    public static Command parseCommandData(String raw) throws InvictaException {
        String trimmed = raw.trim();
        String[] commandString = trimmed.split(" ");
        CommandType commandType = CommandType.fromString(commandString[0]);
        requireInput(commandString[0], MessageKey.MISSING_INPUT);
        assert !(commandString[0].isEmpty()) : "Data for command input missing!";
        switch (commandType) {
        case BYE: {
            return new ExitCommand();
        }
        case LIST, HELP: {
            return new DisplayCommand(commandType);
        }
        case DELETE: {
            return processEditCommand(commandString, commandType, MessageKey.DELETE_USAGE);
        }
        case MARK: {
            return processEditCommand(commandString, commandType, MessageKey.MARK_USAGE);
        }
        case UNMARK: {
            return processEditCommand(commandString, commandType, MessageKey.UNMARK_USAGE);
        }
        case EVENT: {
            return processEventCommand(commandString);
        }
        case DEADLINE: {
            return processDeadlineCommand(commandString);
        }
        case TODO: {
            return processTodoCommand(commandString);
        }
        case FIND: {
            return processFindCommand(commandString, commandType);
        }
        case DAY: {
            return processDayCommand(commandString, commandType);
        }
        case PERIOD: {
            return processPeriodCommand(commandString, commandType);
        }
        default: {
            throw new InvictaException(Message.getChatbotMessage(MessageKey.INVALID_COMMAND,
                    Message.getUsageMessage(MessageKey.TYPE_HELP)));
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
        boolean isEndOfPeriodStart = false;
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
                isEndOfPeriodStart = true;
                argsDoneFlag -= 1;
                if (argsDoneFlag < 1) {
                    break;
                }
            } else if (isEndOfPeriodStart) {
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
