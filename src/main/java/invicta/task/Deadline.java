package invicta.task;

// Imports to handle time data
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDateTime deadlineTime;
    private DateTimeFormatter dateAndTime = DateTimeFormatter.ofPattern("MMM dd yyyy (EEE) hh:mm a ");

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadlineTime = deadline;
    }


    // Getter methods to get deadline
    public LocalDateTime getDeadline() {
        return this.deadlineTime;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadlineTime.format(dateAndTime) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deadline deadline)) {
            return false;
        }

        return isDone == deadline.isDone && description.equals(deadline.description)
                && deadlineTime.equals(deadline.deadlineTime);
    }
}
