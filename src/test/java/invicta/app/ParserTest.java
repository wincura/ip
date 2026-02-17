package invicta.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import invicta.command.AddCommand;
import invicta.command.DisplayCommand;
import invicta.command.EditCommand;


/**
 * Tests that the parser returns the correct command type when parsing command input,
 * returns a valid LocalDateTime object when parsing date time input, and returns
 * a valid LocalDateTime array when parsing period data. Assumes that command type of
 * CommandType enum is correct.
 */
public class ParserTest {

    /**
     * Verifies that the parseCommandData method returns an AddCommand
     * when valid command to add Todo is input.
     */
    @Test
    public void parseCommandData_validTodoCommand_returnCorrectCommand() throws InvictaException {
        String raw = "todo Buy Birthday Gifts";
        assertInstanceOf(AddCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to add Todo is input.
     */
    @Test
    public void parseCommandData_todoMissingName_throwsInvictaException() throws InvictaException {
        String raw = "todo";
        assertThrows(InvictaException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method returns an AddCommand
     * when valid command to add Event is input.
     */
    @Test
    public void parseCommandData_validEventCommand_returnCorrectCommand() throws InvictaException {
        String raw = "event CCA Recruitment Drive /from 2026-02-19 18:30 /to 2026-03-28 20:30";
        assertInstanceOf(AddCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to add Event missing a start time is input.
     */
    @Test
    public void parseCommandData_eventMissingStartTime_throwsInvictaException() throws InvictaException {
        String raw = "event CCA Recruitment Drive";
        assertThrows(InvictaException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to add Event missing an end time is input.
     */
    @Test
    public void parseCommandData_eventMissingEndTime_throwsInvictaException() throws InvictaException {
        String raw = "event CCA Recruitment Drive /from 2026-02-19 18:30";
        assertThrows(InvictaException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method returns an AddCommand
     * when valid command to add Deadline is input.
     */
    @Test
    public void parseCommandData_validDeadlineCommand_returnCorrectCommand() throws InvictaException {
        String raw = "deadline Problem Set 1 /by 2026-02-21 23:59";
        assertInstanceOf(AddCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to add Deadline missing a deadline time is input.
     */
    @Test
    public void parseCommandData_deadlineMissingDeadlineTime_throwsInvictaException() throws InvictaException {
        String raw = "deadline Problem Set 1";
        assertThrows(InvictaException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method returns a DisplayCommand
     * when valid command to list tasks is input.
     */
    @Test
    public void parseCommandData_validListCommand_returnCorrectCommand() throws InvictaException {
        String raw = "list";
        assertInstanceOf(DisplayCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method returns a DisplayCommand
     * when valid command to get help is input.
     */
    @Test
    public void parseCommandData_validHelpCommand_returnCorrectCommand() throws InvictaException {
        String raw = "help";
        assertInstanceOf(DisplayCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method returns an EditCommand
     * when valid command to mark a task is input.
     */
    @Test
    public void parseCommandData_validMarkCommand_returnCorrectCommand() throws InvictaException {
        String raw = "mark 1";
        assertInstanceOf(EditCommand.class, Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to mark a task missing an index is input.
     */
    @Test
    public void parseCommandData_markMissingIndex_throwsInvictaException() throws InvictaException {
        String raw = "mark";
        assertThrows(InvictaException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseCommandData method throws an InvictaException
     * when invalid command to mark a task with an invalid index is input.
     */
    @Test
    public void parseCommandData_markInvalidIndex_throwsNumberFormatException() throws NumberFormatException {
        String raw = "mark a";
        assertThrows(NumberFormatException.class, () -> Parser.parseCommandData(raw));
    }

    /**
     * Verifies that the parseDateTimeData method returns a LocalDateTime
     * with time set to 00:00 when valid date string is input.
     */
    @Test
    public void parseDateTimeData_validDateString_returnCorrectDateTime() {
        String raw = "2026-03-28";
        LocalDateTime expectedDateTime = LocalDateTime.of(
                LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(0, 0));
        LocalDateTime outputDateTime = Parser.parseDateTimeData(raw);
        assertEquals(expectedDateTime, outputDateTime);
    }

    /**
     * Verifies that the parseDateTimeData method returns correct LocalDateTime
     * when valid date time string is input.
     */
    @Test
    public void parseDateTimeData_validDateTimeString_returnCorrectDateTime() {
        String raw = "2026-03-28 20:30";
        LocalDateTime expectedDateTime = LocalDateTime.of(
                LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(20, 30));
        LocalDateTime outputDateTime = Parser.parseDateTimeData(raw);
        assertEquals(expectedDateTime, outputDateTime);
    }

    /**
     * Verifies that the parseDateTimeData method throws a DateTimeParseException
     * when an invalid date time string is input.
     */
    @Test
    public void parseDateTimeData_invalidDateTimeData_throwDateTimeParseException() {
        String raw = "2026-03-aa bb:cc";
        assertThrows(DateTimeParseException.class, () -> Parser.parseDateTimeData(raw));
    }

}
