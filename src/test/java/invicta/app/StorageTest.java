package invicta.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import invicta.task.Deadline;
import invicta.task.Event;
import invicta.task.Task;
import invicta.task.TaskList;
import invicta.task.Todo;

public class StorageTest {
    private static final String VALID_FILE_TO_READ_PATH = "./data/validloadtest.txt";
    private static final String INVALID_FILE_TO_READ_PATH = "./data/invalidloadtest.txt";
    private static final String FILE_TO_WRITE_PATH = "./data/updatetest.txt";
    private static final String EXPECTED_WRITE_FILE_PATH = "./data/validupdatetest.txt";


    private Storage testLoadStorage;
    private Storage testUpdateStorage;
    private TaskList baseTaskList;

    @BeforeEach
    public void setUp() {
        baseTaskList = new TaskList(new ArrayList<Task>());
        baseTaskList.addTask(new Todo("Placeholder 1"));
        baseTaskList.addTask(new Deadline("Placeholder 2", LocalDateTime.of(
                LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(20, 30))));
        baseTaskList.addTask(new Event("Placeholder 3",
                LocalDateTime.of(LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(20, 30)),
                LocalDateTime.of(LocalDate.of(2026, Month.MARCH, 28), LocalTime.of(22, 30))));
        testLoadStorage = null;
        testUpdateStorage = null;
        try {
            Files.deleteIfExists(Path.of((FILE_TO_WRITE_PATH)));
            Files.createFile(Path.of((FILE_TO_WRITE_PATH)));
        } catch (IOException e) {
            System.err.println("Error occurred while writing to file: " + e.getMessage());
        }
    }

    /**
     * Verifies that the load method loads the task list correctly
     * when valid task list file is input.
     */
    @Test
    public void load_validTaskListFile_loadTaskListCorrectly() throws InvictaException, IOException {
        testLoadStorage = new Storage(VALID_FILE_TO_READ_PATH);
        ArrayList<Task> expectedTaskList = baseTaskList.getTaskList();
        ArrayList<Task> output = new TaskList(testLoadStorage.load()).getTaskList();
        for (int i = 0; i < expectedTaskList.size(); i++) {
            assertEquals(expectedTaskList.get(i), output.get(i));
        }
    }

    /**
     * Verifies that the load method  throws an InvictaException
     * when invalid task list file is input.
     */
    @Test
    public void load_invalidTaskListFile_throwInvictaException() throws InvictaException, IOException {
        testLoadStorage = new Storage(INVALID_FILE_TO_READ_PATH);
        assertThrows(InvictaException.class, () -> new TaskList(testLoadStorage.load()));
    }

    /**
     * Verifies that the update method updates the task list file correctly
     * when file path to write is valid.
     */
    @Test
    public void update_validPathToWrite_updateTaskListCorrectly() throws IOException {
        String expected = Files.readString(Path.of(EXPECTED_WRITE_FILE_PATH));
        testUpdateStorage = new Storage(FILE_TO_WRITE_PATH);
        TaskList taskListToWrite = new TaskList();
        for (Task t : baseTaskList.getTaskList()) {
            taskListToWrite.addTask(t);
            testUpdateStorage.update(taskListToWrite);
        }
        String output = Files.readString(Path.of(FILE_TO_WRITE_PATH));
        assertEquals(expected, output);
    }

    /**
     * Verifies that the update method throws an NullPointerException
     * when invalid task list file is input.
     * Note that it does not occur in the app because the file path
     * is fixed upon launch, with file created at path if non-existent.
     */
    @Test
    public void update_invalidPathToWrite_throwNullPointerException() throws NullPointerException, IOException {
        testUpdateStorage = new Storage(FILE_TO_WRITE_PATH);
        TaskList taskListToWrite = new TaskList();
        testUpdateStorage.update(taskListToWrite);
        assertThrows(NullPointerException.class, () -> new TaskList(testLoadStorage.load()));
    }
}
