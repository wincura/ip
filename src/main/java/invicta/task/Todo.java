package invicta.task;

/**
 * Represents a to-do task without a set time.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Todo todo)) {
            return false;
        }

        return isDone == todo.isDone && description.equals(todo.description);
    }
}
