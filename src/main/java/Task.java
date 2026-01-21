public class Task {
    protected String description;
    protected boolean status;

    public Task(String description) {
        this.description = description;
        this.status = false;
    }

    // Getter methods to get description and status
    public String getDescription() {
        return this.description;
    }

    public boolean getStatus() {
        return this.status;
    }

    public String getStatusIcon() {
        return (status ? "X" : " "); // mark done task with X character
    }

    // Setter method to mark/unmark done task
    public void setStatus(boolean newStatus) {
        this.status = newStatus;
    }

    // To be overridden by subclasses
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}