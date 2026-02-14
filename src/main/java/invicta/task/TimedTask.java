package invicta.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a task with associated time data.
 */
public abstract class TimedTask extends Task {
    /**
     * Constructs an instance of Task class.
     * Serves as a base for subclasses.
     *
     */
    public TimedTask(String description) {
        super(description);
    }

    /**
     * Returns true if deadline tasks falls on given date.
     */
    public abstract boolean isOnDate(LocalDate dateToSearch);

    /**
     * Returns true if deadline tasks is within given period.
     */
    public abstract boolean isWithinPeriod(LocalDateTime periodStartTime, LocalDateTime periodEndTime);
}
