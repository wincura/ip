public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    // Getter methods to get description and status
    public String getDescription() {
        return this.description;
    }

    public boolean getDone() {
        return this.isDone;
    }

    public String getDoneIcon() {
        return (isDone ? "X" : " "); // mark done task with X character
    }

    // Setter method to mark/unmark done task
    public void setDone(boolean newStatus) {
        this.isDone = newStatus;
    }

    // To be overridden by subclasses
    @Override
    public String toString() {
        return "[" + this.getDoneIcon() + "] " + this.getDescription();
    }
}