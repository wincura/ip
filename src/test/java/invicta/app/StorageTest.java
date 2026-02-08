package invicta.app;

import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Task;
import invicta.task.TaskList;
import invicta.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import invicta.app.Storage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    private static final String FILE_TO_READ_PATH = "./data/toread.txt";
    private static final String FILE_TO_UPDATE_PATH = "./data/toupdate.txt";


    private Storage testLoadStorage;
    private Storage testUpdateStorage;
    private TaskList baseTaskList;

    @BeforeEach
    public void setUp() {
        testLoadStorage = new Storage(FILE_TO_READ_PATH);
        baseTaskList = new TaskList(new ArrayList<Task>());
        baseTaskList.addTask(new Todo("Placeholder 1"));
        baseTaskList.addTask(new Deadline("Placeholder 2", LocalDateTime.of(
                LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(20, 30))));
        baseTaskList.addTask(new Event("Placeholder 3",
                LocalDateTime.of(LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(20, 30)),
                LocalDateTime.of(LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(22, 30))));
    }

    @Test
    public void load_validTaskListFile_returnCorrectCommand() throws InvictaException, IOException {
        ArrayList<Task> expectedTaskList = baseTaskList.getTaskList();
        TaskList output = new TaskList(new ArrayList<Task>());
        assertEquals(expectedTaskList, output);
    }
}
