package invicta.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDateTime deadline;
    private DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy (EEE) hh:mm a ");

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }


    // Getter methods to get deadline
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline.format(dateAndTime) + ")";
    }
}
