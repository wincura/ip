package invicta.app;

import invicta.command.*;
import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Parser {
    // date time formats and formatters to be used by the chatbot
    public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    public static final String FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_DISPLAY = "MMM dd yyyy (EEE)";
    public static DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern(FORMAT_DATE_ONLY);
    public static DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern(FORMAT_DATE_AND_TIME);
    public static DateTimeFormatter dateDisplay = DateTimeFormatter.ofPattern(FORMAT_DATE_DISPLAY);

    public static Command handleCommandData(String[] commandString, CommandType commandType) throws InvictaException {
        if (commandString.length == 0) {
            throw new InvictaException("_".repeat(100)
                    + "\n\tWhat? Did you say something? Type a message!\n"
                    + "_".repeat(100));
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
                        throw new InvictaException("_".repeat(100)
                                + "\n\tPlease provide an index for this command. (usage: delete <number>)\n"
                                + "_".repeat(100));
                    } else {
                        int index = Integer.parseInt(commandString[1]) - 1;
                        return new EditCommand(commandType, index);
                    }
                }
                case MARK: {
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tPlease provide an index for this command. (usage: mark <number>)\n"
                                + "_".repeat(100));
                    } else {
                        int index = Integer.parseInt(commandString[1]) - 1;
                        return new EditCommand(commandType, index);
                    }
                }
                case UNMARK: {
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tPlease provide an index for this command. (usage: unmark <number>)\n"
                                + "_".repeat(100));
                    } else {
                        int index = Integer.parseInt(commandString[1]) - 1;
                        return new EditCommand(commandType, index);
                    }
                }
                case EVENT: {
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tMissing task name, start time and end time! (usage: event <name> /from <start> /to <end>)\n"
                                + "_".repeat(100));
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
                            return new AddCommand(ev);
                        }
                    }
                }
                case DEADLINE: {
                    if (commandString.length < 2) {
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
                            throw new InvictaException("_".repeat(100)
                                    + "\n\tMissing deadline! (usage: deadline <name> /by <deadline>)\n"
                                    + "_".repeat(100));
                        } else {
                            LocalDateTime deadlineTime = Parser.handleDateTimeData(deadlineTimeString.toString().trim());
                            Deadline dl = new Deadline(taskName.toString().trim(),
                                    deadlineTime);
                            return new AddCommand(dl);
                        }
                    }
                }
                case TODO: {
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tMissing task name! (usage: todo <name>)\n"
                                + "_".repeat(100));
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
                case DAY: {
                    LocalDate dateToSearch;
                    StringBuilder dateToSearchString = new StringBuilder();
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tMissing date! (usage: day <date>)\n"
                                + "_".repeat(100));
                    } else {
                        for (int i = 1; i < commandString.length; i++) {
                            String word = commandString[i];
                            dateToSearchString.append(word).append(" ");
                        }
                        dateToSearch = Parser.handleDateTimeData(dateToSearchString
                                .toString().trim()).toLocalDate(); // time values are disregarded
                        return new DisplayCommand(commandType, dateToSearch);
                    }
                }
                case PERIOD: {
                    if (commandString.length < 2) {
                        throw new InvictaException("_".repeat(100)
                                + "\n\tMissing start time and end time! (usage: period /from <start> /to <end>)\n"
                                + "_".repeat(100));
                    } else {
                        String[] periodInput = Arrays.copyOfRange(commandString,1, commandString.length);
                        String[] period = Parser.handlePeriodInput(periodInput);
                        if (period[0].isEmpty()) {
                            throw new InvictaException("_".repeat(100)
                                    + "\n\tMissing start time and end time! (usage: period /from <start> /to <end>)\n"
                                    + "_".repeat(100));
                        } else if (period[1].isEmpty()) {
                            throw new InvictaException("_".repeat(100)
                                    + "\n\tMissing end time! (usage: period /from <start> /to <end>)" + "\n"
                                    + "_".repeat(100));
                        }
                        LocalDateTime periodStartTime = Parser.handleDateTimeData(period[0].trim());
                        LocalDateTime periodEndTime = Parser.handleDateTimeData(period[1].trim());
                        return new DisplayCommand(commandType, periodStartTime, periodEndTime);
                    }
                }
                default: {
                    throw new InvictaException("_".repeat(100)
                            + "\n\tMissing end time! (usage: period /from <start> /to <end>)" + "\n"
                            + "_".repeat(100));
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
     * @throws DateTimeParseException Exception thrown when string is of invalid format.
     */
    public static LocalDateTime handleDateTimeData(String dateTimeString) {
        if (dateTimeString.length() <= 10) {
            return LocalDate.parse(dateTimeString, dateOnly).atStartOfDay();
        } else {
            return LocalDateTime.parse(dateTimeString, dateAndTime);
        }
    }

    /**
     * Iterates through user input to extract strings representing start and end times of a period.
     * If more user arguments in input than expected, they are disregarded.
     *
     * @param userInput String array to be parsed into period start and end times.
     * @return period String array containing the strings representing start and end times of a period.
     */
    public static String[] handlePeriodInput(String[] userInput) {
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
