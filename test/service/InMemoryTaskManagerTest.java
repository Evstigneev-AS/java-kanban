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
        // setup - object with which we will work and the input data
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);

        // act - perform the operation we are testing
        Long taskId = inMemoryTaskManager.createTask(task);

        // verify - statements about the correctness of an action
        Task byId = inMemoryTaskManager.taskById(taskId);
        assertEquals(byId, task);
    }

    @Test
    public void createEpicCheckEpicAdded() {
        // setup - object with which we will work and the input data
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", NEW);

        // act - perform the operation we are testing
        Long epicId = inMemoryTaskManager.createEpic(epic);

        // verify - statements about the correctness of an action
        Task byId = inMemoryTaskManager.taskById(epicId);
        assertEquals(byId, epic);
    }

    @Test
    public void createSubtaskCheckSubtaskAdded() {
        // setup - object with which we will work and the input data
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", NEW, epic);

        // act - perform the operation we are testing
        Long subtaskId = inMemoryTaskManager.createSubtask(subtask);

        // verify - statements about the correctness of an action
        Task byId = inMemoryTaskManager.taskById(subtaskId);
        assertEquals(byId, subtask);
    }

    @Test
    public void createTaskCheckEveryField() {
        // setup - object with which we will work and the input data
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        // act - perform the operation we are testing
        Long taskId = inMemoryTaskManager.createTask(new Task(47L, "Test addNewTask", "Test addNewTask description", NEW));

        // verify - statements about the correctness of an action
        Task byId = inMemoryTaskManager.taskById(taskId);
        assertEquals(byId.getId(), 47L);
        assertEquals(byId.getName(), "Test addNewTask");
        assertEquals(byId.getDescription(), "Test addNewTask description");
        assertEquals(byId.getStatus(), NEW);
    }

    @Test
    void createTaskPutOnListTasksCheckListTasks() {
        //setup - object with which we will work and the input data
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);

        //act - perform the operation we are testing
        taskManager.createTask(task);

        //verify - statements about the correctness of an action
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpicPutOnListEpicCheckListEpic() {
        //setup - object with which we will work and the input data
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", NEW);

        //act - perform the operation we are testing
        taskManager.createEpic(epic);

        //verify - statements about the correctness of an action
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubtaskPutOnListSubtaskCheckListSubtask() {
        //setup - object with which we will work and the input data
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpicSubtask", "Test addNewEpic description", NEW);
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewEpic description", NEW, epic);

        //act - perform the operation we are testing
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        //verify - statements about the correctness of an action
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void deleteId() {
        //setup - object with which we will work and the input data
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);

        //act - perform the operation we are testing
        taskManager.createTask(task);

        //verify - statements about the correctness of an action
        taskManager.delete(0L);
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllTask() {
        //setup - object with which we will work and the input data
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Task task0 = new Task("Test addNewTask", "Test addNewTask description", NEW);
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", NEW);

        //act - perform the operation we are testing
        taskManager.createTask(task0);
        taskManager.createTask(task1);

        //verify - statements about the correctness of an action
        taskManager.deleteAllTasks();
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllEpic() {
        //setup - object with which we will work and the input data
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Epic epic0 = new Epic("Test addNewEpicSubtask", "Test addNewEpic description", NEW);
        Epic epic1 = new Epic("Test addNewEpicSubtask", "Test addNewEpic description", NEW);

        //act - perform the operation we are testing
        taskManager.createEpic(epic0);
        taskManager.createEpic(epic1);

        //verify - statements about the correctness of an action
        taskManager.deleteAllEpic();
        final List<Epic> epics = taskManager.getAllEpic();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllSubtask() {
        //setup - object with which we will work and the input data
        Managers.setTaskManager(null);
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Test addNewEpicSubtask", "Test addNewEpic description", NEW);
        Subtask subtask0 = new Subtask("Test addNewSubtask", "Test addNewEpic description", NEW, epic);
        Subtask subtask1 = new Subtask("Test addNewSubtask", "Test addNewEpic description", NEW, epic);

        //act - perform the operation we are testing
        taskManager.createSubtask(subtask0);
        taskManager.createSubtask(subtask1);

        //verify - statements about the correctness of an action
        taskManager.deleteAllSubtask();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
    }
}