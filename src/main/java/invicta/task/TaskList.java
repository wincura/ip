package invicta.task;

// Imports to use data structures
import java.util.ArrayList;

// Imports to handle time data
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a collection of tasks.
 * Handles adding, modifying, removing and querying of tasks.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> loaded) {
        this.taskList = loaded;
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    public void addTask(Task t) {
        this.taskList.add(t);
    }

    public void removeTask(int i) {
        this.taskList.remove(i);
    }

    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    public String getTaskString(int i) {
        return this.taskList.get(i).toString();
    }

    public int getSize() {
        return this.taskList.size();
    }

    public boolean getDone(int i) {
        return this.taskList.get(i).getDone();
    }

    public void setDone(int i, boolean newStatus) {
        this.taskList.get(i).setDone(newStatus);
    }

    /**
     * Returns a collection of tasks with the queried string in the description.
     *
     * @param searchString keyword to query matching strings
     * @return collection of tasks containing search string
     */
    public ArrayList<Task> getMatchingTasks(String searchString) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task t : this.taskList) {
            if (t.getDescription().toLowerCase().contains(searchString.toLowerCase())) {
                foundTasks.add(t);
            }
        }
        return foundTasks;
    }

    /**
     * Returns a collection of tasks that fall on the date.
     * Includes Deadline tasks with equal deadline date to the given date,
     * and any Event tasks whose duration touches the given date.
     *
     * @param dateToSearch date to be searched.
     * @return Collection of tasks that fall on the date.
     */
    public ArrayList<Task> getOnDateTasks(LocalDate dateToSearch) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        // add the tasks to temp ArrayList of Tasks to be displayed
        for (Task t : this.taskList) {
            if (t instanceof Deadline) {
                if (((Deadline) t).getDeadline().toLocalDate().isEqual(dateToSearch)) {
                    foundTasks.add(t);
                }
            }
            if (t instanceof Event e) {
                LocalDate startDate = e.getStart().toLocalDate();
                LocalDate endDate = e.getEnd().toLocalDate();
                // using inclusive checks ensures not missing events that fall on the start and end dates
                boolean startsOnOrBeforeDate = startDate.isEqual(dateToSearch)
                        || startDate.isBefore(dateToSearch);
                boolean endsOnOrAfterDate = endDate.isEqual(dateToSearch)
                        || endDate.isAfter(dateToSearch);
                if (startsOnOrBeforeDate && endsOnOrAfterDate) {
                    foundTasks.add(t);
                }
            }
        }
        return foundTasks;
    }

    /**
     * Returns a collection of tasks that fall within the given period.
     * Includes Deadline tasks with deadline date within the given period,
     * and any Event tasks whose duration touches the given period.
     *
     * @param periodStartTime start time of period to be searched.
     * @param periodEndTime end time of period to be searched.
     * @return Collection of tasks that fall on the date.
     */
    public ArrayList<Task> getInPeriodTasks(LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        ArrayList<Task> foundTasks = new ArrayList<>();

        // add the tasks to temp ArrayList of Tasks to be displayed
        for (Task t : this.taskList) {
            if (t instanceof Deadline d) {
                LocalDateTime deadline = d.getDeadline();
                boolean afterOrOnStart = deadline.isEqual(periodStartTime)
                        || deadline.isAfter(periodStartTime);
                boolean beforeOrOnEnd = deadline.isEqual(periodEndTime)
                        || deadline.isBefore(periodEndTime);
                if (afterOrOnStart && beforeOrOnEnd) {
                    foundTasks.add(t);
                }
            }
            if (t instanceof Event e) {
                LocalDateTime start = e.getStart();
                LocalDateTime end = e.getEnd();
                // using inclusive checks and start time to check ensures not missing events that extend beyond period
                boolean startsBeforeOrOnPeriodEnd =
                        start.isBefore(periodEndTime) || start.isEqual(periodEndTime);
                boolean endsAfterOrOnPeriodStart =
                        end.isAfter(periodStartTime) || end.isEqual(periodStartTime);
                if (startsBeforeOrOnPeriodEnd && endsAfterOrOnPeriodStart) {
                    foundTasks.add(t);
                }
            }
        }
        return foundTasks;
    }

    /**
     * Prints the details of each task in provided Task List.
     */
    public void printTasks() {

    }
}
