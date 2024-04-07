package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.Task.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {

    @Test
    public void createTaskCheckTaskAdded() {
        // setup
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);

        // act
        Long taskId = inMemoryTaskManager.createTask(task);

        // verify
        Task byId = inMemoryTaskManager.taskById(taskId);
        assertEquals(byId, task);
    }

    @Test
    public void createEpicCheckEpicAdded() {
        // setup
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", NEW);

        // act
        Long epicId = inMemoryTaskManager.createEpic(epic);

        // verify
        Task byId = inMemoryTaskManager.taskById(epicId);
        assertEquals(byId, epic);
    }

    @Test
    public void createSubtaskCheckSubtaskAdded() {
        // setup
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", NEW, epic);

        // act
        Long subtaskId = inMemoryTaskManager.createSubtask(subtask);

        // verify
        Task byId = inMemoryTaskManager.taskById(subtaskId);
        assertEquals(byId, subtask);
    }

    @Test
    public void createTaskCheckEveryField() {
        // setup
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        // act
        Long taskId = inMemoryTaskManager.createTask(new Task(47L, "Test addNewTask", "Test addNewTask description", NEW));

        // verify
        Task byId = inMemoryTaskManager.taskById(taskId);
        assertEquals(byId.getId(), 47L);
        assertEquals(byId.getName(), "Test addNewTask");
        assertEquals(byId.getDescription(), "Test addNewTask description");
        assertEquals(byId.getStatus(), NEW);
    }

    @Test
    void createTaskPutOnListTasksCheckListTasks() {
        //setup
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);

        //act
        taskManager.createTask(task);

        //verify
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpicPutOnListEpicCheckListEpic() {
        //setup
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);

        //act
        taskManager.createEpic(epic);

        //verify
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubtaskPutOnListSubtaskCheckListSubtask() {
        //setup
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpicSubtask", "Test addNewEpic description", NEW);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewEpic description", NEW, epic);

        //act
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        //verify
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }
}