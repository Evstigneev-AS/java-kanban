package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static model.Type.SUBTASK;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    String fileTask = "resources/fileTask.csv";
    Long startId = 0L;

    public FileBackedTaskManager() {
        super();
        createFile();
        readerFile();
    }

    public FileBackedTaskManager(String path) {
        super();
        this.fileTask = path;
        createFile();
        readerFile();
    }

    void createFile() {
        try {
            File files = new File(fileTask);
            if (!files.exists()) {
                FileWriter writer = new FileWriter(fileTask);
                writer.write("id, type, name, status, description, epic id\n");
                writer.close();
                System.out.println("файл создан: " + fileTask);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }

    void readerFile() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileTask));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            Task task = getRecordFromLine(scanner.nextLine());
            super.table.put(task.getId(), task);
        }
        super.id = startId + 1;
        scanner.close();
    }

    private Task getRecordFromLine(String line) {
        String[] split = line.split(", ");
        long id = Long.parseLong(split[0]);
        if (id > startId) {
            startId = id;
        }
        if ("SUBTASK".equalsIgnoreCase(split[1])) {
            Long epicId = (Long.parseLong(split[5]));
            Epic epic = (Epic) super.table.get(epicId);
            String status = split[3];
            Task.Status status1 = Task.Status.valueOf(status);
            Subtask subtask = new Subtask(id, split[2], split[4], status1, epic);
            return subtask;
        }
        if ("EPIC".equalsIgnoreCase(split[1])) {
            String status = split[3];
            Task.Status status1 = Task.Status.valueOf(status);
            Epic epic = new Epic(id, split[2], split[4], status1);
            return epic;
        }
        String status = split[3];
        Task.Status status1 = Task.Status.valueOf(status);
        Task task = new Task(id, split[2], split[4], status1);
        return task;
    }

    void writeFileTask() {
        try {
            FileWriter writer = new FileWriter(new File(fileTask), false);
            writer.write("id, type, name, status, description, epic id\n");
            for (Task task : table.values()) {
                String epicId = "";
                if (SUBTASK.equals(task.getType())) {
                    Subtask subtask = (Subtask) task;
                    epicId = subtask.getEpic().getId().toString();
                }
                writer.write(task.getId() + ", ");
                writer.write(task.getType() + ", ");
                writer.write(task.getName() + ", ");
                writer.write(task.getStatus() + ", ");
                writer.write(task.getDescription() + ", ");
                writer.write(epicId + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long createTask(Task task) {
        Long taskId = super.createTask(task);
        writeFileTask();
        return taskId;
    }

    @Override
    public Long createEpic(Epic epic) {
        Long epicId = super.createEpic(epic);
        writeFileTask();
        return epicId;
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        Long subtaskId = super.createSubtask(subtask);
        writeFileTask();
        return subtaskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        writeFileTask();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        writeFileTask();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        writeFileTask();
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        writeFileTask();
    }

    @Override
    public void deleteEpicById(Long id) {
        super.deleteEpicById(id);
        writeFileTask();
    }

    @Override
    public void deleteSubtaskId(Long id) {
        super.deleteSubtaskId(id);
        writeFileTask();
    }

    HashMap<Long, Task> getTable() {
        return table;
    }
}
