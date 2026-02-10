package invicta.task;

// Imports to handle time data
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;
    private DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy (EEE) hh:mm a ");

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    // Getter methods to get start and end times
    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }


    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start.format(dateAndTime) + " to: " + this.end.format(dateAndTime) + ")";
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
