package invicta.app;

import invicta.task.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private String filePath;
    private String dirPath;

    public Storage(String filePath) {
        this.filePath = filePath;
        this.dirPath = Paths.get(filePath).getParent().toString();
    }

    /**
     * Loads task list from provided file.
     * If the file does not exist, it is created.
     * Exceptions are caught and handled in calling method.
     *
     * @return loaded invicta.task.Task List loaded with stored task data in file.
     */
    public ArrayList<Task> load() throws IOException, InvictaException {
        ArrayList<Task> loaded = new ArrayList<>();
        File dir = new File(this.dirPath);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Data directory created at: " + dir.getAbsolutePath());
            } else {
                System.out.println("Failed to create new directory.");
            }
        } else {
            System.out.println("Data directory found at: " + dir.getAbsolutePath());
        }

        File file = new File(this.filePath);
        if (file.createNewFile()) {
            System.out.println("invicta.task.Task list file created at: " + file.getAbsolutePath());
        } else {
            System.out.println("invicta.task.Task list file found at: " + file.getAbsolutePath()
                    + "\nLoading data from file into Invicta...");
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                String[] input = s.nextLine().split(";");
                Type type = Type.fromString(input[0]);
                switch (type) {
                    case TODO: {
                        boolean isDone = input[1].equals("1");
                        String name = input[2];
                        Todo toAdd = new Todo(name);
                        if (isDone) {
                            toAdd.setDone(true);
                        }
                        loaded.add(toAdd);
                        break;
                    }
                    case DEADLINE: {
                        boolean isDone = input[1].equals("1");
                        String name = input[2];
                        LocalDateTime deadline = Parser.handleDateTimeData(input[3]);
                        Deadline toAdd = new Deadline(name, deadline);
                        if (isDone) {
                            toAdd.setDone(true);
                        }
                        loaded.add(toAdd);
                        break;
                    }
                    case EVENT: {
                        boolean isDone = input[1].equals("1");
                        String name = input[2];
                        LocalDateTime start = Parser.handleDateTimeData(input[3]);
                        LocalDateTime end = Parser.handleDateTimeData(input[4]);
                        Event toAdd = new Event(name, start, end);
                        if (isDone) {
                            toAdd.setDone(true);
                        }
                        loaded.add(toAdd);
                        break;
                    }
                }
            }
            System.out.println("invicta.task.Task list file data successfully loaded into Invicta.\n\n\n");
        }
        return loaded;
    }

    /**
     * Writes into task list file to reflect changes in task list.
     */
    public void updateTaskListFile(TaskList taskList) {
        ArrayList<Task> updatedTaskList = taskList.getTaskList();
        try {
            FileWriter fw = new FileWriter(this.filePath);
            String toAdd;
            for (Task t : updatedTaskList) {
                if (t instanceof Todo) {
                    String[] values = {Type.TODO.getCode(), (t.getDone()) ? "1" : "0", t.getDescription()};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                } else if (t instanceof Deadline) {
                    String[] values = {Type.DEADLINE.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                            ((Deadline) t).getDeadline().format(Parser.dateAndTime)};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                } else if (t instanceof Event) {
                    String[] values = {Type.DEADLINE.getCode(), (t.getDone()) ? "1" : "0", t.getDescription(),
                            ((Event) t).getStart().format(Parser.dateAndTime), ((Event) t).getEnd().format(Parser.dateAndTime)};
                    toAdd = String.join(";", values);
                    fw.write(System.lineSeparator() + toAdd);
                }
            }
            fw.close();
        } catch (IOException e) {
            System.out.print("Error occurred while writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
