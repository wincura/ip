package invicta.task;

// Imports to use data structures and to handle time data
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of tasks.
 * Handles adding, modifying, removing and querying of tasks.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Differentiates type of query to perform when processing task search queries.
     */
    private enum QueryContext {
        MATCH,
        DATE,
        PERIOD
    }
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> loaded) {
        this.taskList = loaded;
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    /**
     * Adds task to task list.
     */
    public void addTask(Task t) {
        this.taskList.add(t);
        assert this.taskList.contains(t) : "Task was not added properly!";
    }

    /**
     * Removes task from task list.
     */
    public void removeTask(int i) {
        Task temp = this.taskList.get(i);
        this.taskList.remove(i);
        assert !this.taskList.contains(temp) : "Task was not removed properly!";
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
        List<Task> foundTasks = taskList.stream()
                .filter(t -> t.getDescription().toLowerCase().contains(searchString.toLowerCase()))
                .toList();
        return (ArrayList<Task>) foundTasks;
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
            processQuery(t, dateToSearch, foundTasks);
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
            processQuery(t, periodStartTime, periodEndTime, foundTasks);
        }
        return foundTasks;
    }

    /**
     * Returns a list of tasks that falls on or overlaps the given date
     *
     * @param t Current task to process.
     * @param dateToSearch Given date to be searched.
     * @param tasks Task list to add current task to if it meets search criteria.
     */
    public void processQuery(Task t, LocalDate dateToSearch, ArrayList<Task> tasks) {
        if (!(t instanceof TimedTask tt)) {
            return;
        }
        if (tt.isOnDate(dateToSearch)) {
            tasks.add(t);
        }
    }

    /**
     * Returns a list of tasks that falls within or overlaps the given period.
     *
     * @param t Current task to process.
     * @param periodStartTime Start time of given period.
     * @param periodEndTime End time of given period.
     * @param tasks Task list to add current task to if it meets search criteria.
     */
    public void processQuery(Task t, LocalDateTime periodStartTime,
                             LocalDateTime periodEndTime, ArrayList<Task> tasks) {
        if (!(t instanceof TimedTask tt)) {
            return;
        }
        if (tt.isWithinPeriod(periodStartTime, periodEndTime)) {
            tasks.add(t);
        }
    }
}
