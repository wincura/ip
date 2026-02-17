package invicta.task;

// Imports to handle time data
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a set start and end time.
 */
public class Event extends TimedTask {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy (EEE) hh:mm a ");

    /**
     * Constructs an instance of Event class.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;

        assert this.description != null;
        assert this.start != null;
        assert this.end != null;
    }

    // Getter methods to get start and end times
    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Returns true if deadline tasks falls on given date.
     */
    @Override
    public boolean isOnDate(LocalDate dateToSearch) {
        LocalDate startDate = this.start.toLocalDate();
        LocalDate endDate = this.end.toLocalDate();

        boolean startsOnOrBeforeDate = startDate.isEqual(dateToSearch)
                || startDate.isBefore(dateToSearch);
        boolean endsOnOrAfterDate = endDate.isEqual(dateToSearch)
                || endDate.isAfter(dateToSearch);

        boolean isOverlapping = startsOnOrBeforeDate && endsOnOrAfterDate;
        assert !(start.isAfter(end)) : "Event start time is after event end time!";
        return isOverlapping;
    }

    /**
     * Returns true if deadline tasks is within given period.
     */
    @Override
    public boolean isWithinPeriod(LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        LocalDateTime start = this.start;
        LocalDateTime end = this.end;

        boolean hasEventStartBeforeOrOnPeriodEnd =
                start.isBefore(periodEndTime) || start.isEqual(periodEndTime);
        boolean hasEventEndAfterOrOnPeriodStart =
                end.isAfter(periodStartTime) || end.isEqual(periodStartTime);

        boolean isOverlapping = hasEventStartBeforeOrOnPeriodEnd && hasEventEndAfterOrOnPeriodStart;
        assert !(start.isAfter(end)) : "Event start time is after event end time!";
        return isOverlapping;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.start.format(dateAndTime)
                + " to: " + this.end.format(dateAndTime) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }

        return isDone == event.isDone && description.equals(event.description)
                && start.equals(event.start) && end.equals(event.end);
    }
}
