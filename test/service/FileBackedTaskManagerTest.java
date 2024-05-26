package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager fileBackedTaskManager;
    String absolutePath;

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        absolutePath = tempFile.getAbsolutePath();
        tempFile.delete();
        fileBackedTaskManager = new FileBackedTaskManager(absolutePath);
    }

    @AfterEach
    void tearDown() {
        File file = new File(absolutePath);
        file.delete();
    }

    @Test
    public void createFailCheckFailAdded() throws IOException {
        // setup - object with which we will work and the input data
        File file = new File(absolutePath);
        file.delete();

        // act - perform the operation we are testing
        fileBackedTaskManager.createFile();

        // verify - statements about the correctness of an action
        assertTrue(new File(absolutePath).exists());
        Scanner scanner = new Scanner(new File(absolutePath));
        String s = scanner.nextLine();
        assertEquals("id, type, name, status, description, duration, startTime, epic id", s);
    }

    @Test
    public void testReaderFileTask() throws IOException {
        // setup - object with which we will work and the input data
        String line = "1, TASK, Task name, DONE, Task description, ";
        FileWriter writer = new FileWriter(absolutePath, true);
        writer.write(line);
        writer.close();

        // act - perform the operation we are testing
        fileBackedTaskManager.readerFile();

        // verify - statements about the correctness of an action
        HashMap<Long, Task> table = fileBackedTaskManager.getTable();
        Task task = new Task(1L, "Task name", "Task description", Task.Status.DONE);
        assertEquals(task, table.get(1L));
    }

    @Test
    public void testWriteFileTask() throws FileNotFoundException {
        // setup - object with which we will work and the input data
        Task task = new Task(1L, "Task name", "Task description", Task.Status.DONE);
        Epic epic = new Epic(2L, "Task name", "Task description", Task.Status.DONE);
        Subtask subtask = new Subtask(3L, "Task name", "Task description", Task.Status.DONE, epic);
        HashMap<Long, Task> table = fileBackedTaskManager.getTable();
        table.put(1L, task);
        table.put(2L, epic);
        table.put(3L, subtask);

        // act - perform the operation we are testing
        fileBackedTaskManager.writeFileTask();

        // verify - statements about the correctness of an action
        Scanner scanner = new Scanner(new File(absolutePath));
        String s = scanner.nextLine();
        assertEquals("id, type, name, status, description, duration, startTime, epic id", s);
        s = scanner.nextLine();
        assertEquals("1, TASK, Task name, DONE, Task description, , , ", s);
        s = scanner.nextLine();
        assertEquals("2, EPIC, Task name, DONE, Task description, , , ", s);
        s = scanner.nextLine();
        assertEquals("3, SUBTASK, Task name, DONE, Task description, , , 2", s);
    }

}
