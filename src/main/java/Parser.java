import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    // date time formats and formatters to be used by the chatbot
    public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    public static final String FORMAT_DATE_AND_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_DISPLAY = "MMM dd yyyy (EEE)";
    public static DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern(FORMAT_DATE_ONLY);
    public static DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern(FORMAT_DATE_AND_TIME);
    public static DateTimeFormatter dateDisplay = DateTimeFormatter.ofPattern(FORMAT_DATE_DISPLAY);

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
