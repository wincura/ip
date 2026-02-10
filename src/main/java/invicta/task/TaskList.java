package invicta.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a collection of tasks.
 * Handles adding, modifying, removing and querying of tasks.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<Task>();
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

    public ArrayList<Task> getOnDateTasks(LocalDate dateToSearch) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        // add the tasks to temp ArrayList of Tasks to be displayed
        for (Task t : this.taskList) {
            if (t instanceof Deadline) {
                if (((Deadline) t).getDeadline().toLocalDate().isEqual(dateToSearch)) {
                    foundTasks.add(t);
                }
            }
            if (t instanceof Event) {
                // using inclusive checks ensures not missing events that fall on the start and end dates
                if ((((Event) t).getStart().toLocalDate().isEqual(dateToSearch)
                        || ((Event) t).getStart().toLocalDate().isBefore(dateToSearch))
                        && (((Event) t).getEnd().toLocalDate().isEqual(dateToSearch)
                        || ((Event) t).getEnd().toLocalDate().isAfter(dateToSearch))) {
                    foundTasks.add(t);
                }
            }
        }
        return foundTasks;
    }

    public ArrayList<Task> getInPeriodTasks(LocalDateTime periodStartTime, LocalDateTime periodEndTime) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        // add the tasks to temp ArrayList of Tasks to be displayed
        for (Task t : this.taskList) {
            if (t instanceof Deadline) {
                if ((((Deadline) t).getDeadline().isEqual(periodStartTime)
                        || ((Deadline) t).getDeadline().isAfter(periodStartTime))
                        && ((((Deadline) t).getDeadline().isEqual(periodEndTime)
                        || ((Deadline) t).getDeadline().isBefore(periodEndTime)))) {
                    foundTasks.add(t);
                }
            }
            if (t instanceof Event) {
                // using inclusive checks and start time to check ensures not missing events that extend beyond period
                if ((((Event) t).getStart().isEqual(periodStartTime) || ((Event) t).getEnd().isEqual(periodStartTime))
                        || (((Event) t).getStart().isEqual(periodEndTime) || ((Event) t).getEnd().isEqual(periodEndTime))
                        || (((Event) t).getEnd().isAfter(periodStartTime)) && ((Event) t).getStart().isBefore(periodStartTime)
                        || (((Event) t).getStart().isAfter(periodStartTime)) && ((Event) t).getEnd().isBefore(periodEndTime)
                        || (((Event) t).getStart().isBefore(periodEndTime)) && ((Event) t).getEnd().isAfter(periodEndTime)
                        || (((Event) t).getStart().isBefore(periodStartTime)) && ((Event) t).getEnd().isAfter(periodEndTime)) {
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
        int number = 0;
        for (Task t : this.taskList) {
            number += 1;
            System.out.println("\t" + number + ". " + t.toString());
        }
    }
}
