package invicta.app;

// Imports to handle file read and write, including date time data
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

// Imports to use task and task list data for storage
import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Task;
import invicta.task.TaskList;
import invicta.task.Todo;
import invicta.app.Message.MessageKey;

/**
 * Handles loading and updating of task list files.
 */
public class Storage {
    private String filePath;
    private String dirPath;

    public Storage(String filePath) {
        this.filePath = filePath;
        this.dirPath = Paths.get(filePath).getParent().toString();
    }

    /**
     * Prepares directory, creating it if it does not exist yet.
     */
    private void prepareDirectory(File dir) {
        if (dir.mkdir()) {
            Ui.showIOMessages(MessageKey.DIRECTORY_CREATED, dir.getAbsolutePath());
        } else {
            Ui.showIOMessages(MessageKey.DIRECTORY_CREATE_FAILED, "");
        }
    }

    /**
     * Reads the file to load the file into task list.
     */
    private void readFile(Scanner s, ArrayList<Task> loadedTasks) throws InvictaException {
        String[] input = s.nextLine().split(";");
        TaskType taskType = TaskType.fromString(input[0]);
        switch (taskType) {
        case TODO: {
            boolean isDone = input[1].equals("1");
            String name = input[2];
            Todo toAdd = new Todo(name);
            if (isDone) {
                toAdd.setDone(true);
            }
            loadedTasks.add(toAdd);
            break;
        }
        case DEADLINE: {
            boolean isDone = input[1].equals("1");
            String name = input[2];
            LocalDateTime deadline = Parser.parseDateTimeData(input[3]);
            Deadline toAdd = new Deadline(name, deadline);
            if (isDone) {
                toAdd.setDone(true);
            }
            loadedTasks.add(toAdd);
            break;
        }
        case EVENT: {
            boolean isDone = input[1].equals("1");
            String name = input[2];
            LocalDateTime start = Parser.parseDateTimeData(input[3]);
            LocalDateTime end = Parser.parseDateTimeData(input[4]);
            Event toAdd = new Event(name, start, end);
            if (isDone) {
                toAdd.setDone(true);
            }
            loadedTasks.add(toAdd);
            break;
        }
        }
    }

    /**
     * Loads task list from provided file.
     * If the file does not exist, it is created.
     * Exceptions are caught and handled in calling method.
     *
     * @return loaded Task List loaded with stored task data in file.
     */
    public ArrayList<Task> load() throws IOException, InvictaException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File dir = new File(this.dirPath);
        if (!dir.exists()) {
            prepareDirectory(dir);
        } else {
            Ui.showIOMessages(MessageKey.DIRECTORY_FOUND, dir.getAbsolutePath());
        }

        File file = new File(this.filePath);
        if (file.createNewFile()) {
            Ui.showIOMessages(MessageKey.FILE_CREATED, dir.getAbsolutePath());
        } else {
            Ui.showIOMessages(MessageKey.FILE_FOUND, dir.getAbsolutePath());
            Ui.showIOMessages(MessageKey.FILE_LOADING, "");
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                readFile(s, loadedTasks);
            }
            s.close();
            Ui.showIOMessages(MessageKey.FILE_LOADED, "");
        }
        return loadedTasks;
    }

    /**
     * Writes current task into task list file.
     */
    private void writeFile(FileWriter fw, Task t) throws IOException {
        String toAdd;
        String[] values = new String[0];
        if (t instanceof Todo) {
            values = new String[]{TaskType.TODO.getCode(), (t.getDone()) ? "1" : "0", t.getDescription()};

        } else if (t instanceof Deadline) {
            values = new String[]{TaskType.DEADLINE.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                    ((Deadline) t).getDeadline().format(Parser.dateAndTime)};
        } else if (t instanceof Event) {
            values = new String[]{TaskType.EVENT.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                    ((Event) t).getStart().format(Parser.dateAndTime),
                    ((Event) t).getEnd().format(Parser.dateAndTime)};
        }
        toAdd = String.join(";", values);
        fw.write(toAdd + System.lineSeparator());
    }

    /**
     * Writes into task list file to reflect changes in task list.
     */
    public void update(TaskList taskList) throws IOException{
        ArrayList<Task> updatedTaskList = taskList.getTaskList();
        FileWriter fw = new FileWriter(this.filePath);
        for (Task t : updatedTaskList) {
            writeFile(fw, t);
        }
        fw.close();
    }
}
