import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager manager = managers.getDefault();
        HistoryManager historyTask = managers.getDefaultHistory();

        Long id1 = manager.createTask(new Task("Поход в магазин", "Купить хлеб", Task.Status.NEW));
        Long id2 = manager.createTask(new Task("Поход в гараж", "Зарядить аккумулятор",
                Task.Status.NEW));
        Epic epic = new Epic("Ремонт квартиры", "Необходимо приобрести",
                Task.Status.NEW);
        Long id3 = manager.createEpic(epic);
        Long id4 = manager.createSubtask(new Subtask("Нужен цемент", "купить 30 мешков цемента по 25 кг!",
                Task.Status.NEW, epic));
        Long id5 = manager.createSubtask(new Subtask("Нужны шурупы", "купить 150 шт. шурупов М8х100!",
                Task.Status.NEW, epic));
        Epic epic1 = new Epic("Устроиться на работу", "Поиск работы", Task.Status.NEW);
        Long id6 = manager.createEpic(epic1);
        Long id7 = manager.createSubtask(new Subtask("Нужен резюме", "Найти шаблон резюме!",
                Task.Status.NEW, epic1));


        manager.taskById(id4);
        manager.taskById(id4);
        manager.taskById(id5);
        manager.taskById(id5);
        manager.taskById(id3);
        manager.taskById(id3);
        manager.taskById(id1);
        manager.taskById(id1);
        manager.taskById(id2);
        manager.taskById(id2);

        printAllTasks(manager, historyTask);
        manager.updateTask(new Task(id1, "Поход в магазин", "Купить хлеб", Task.Status.IN_PROGRESS));
        manager.updateTask(new Task(id2, "Поход в гараж", "Зарядить аккумулятор", Task.Status.DONE));
        manager.updateSubtask(new Subtask(id4, "Нужен цемент", "купить 30 мешков цемента по 25 кг!",
                Task.Status.DONE, epic));
        manager.updateSubtask(new Subtask(id5, "Нужны шурупы", "купить 150 шт. шурупов М8х100!",
                Task.Status.DONE, epic));
        manager.updateSubtask(new Subtask(id7, "Нужен резюме", "Найти шаблон резюме!",
                Task.Status.IN_PROGRESS, epic1));
        printAllTasks(manager, historyTask);


        manager.delete(id1);
        manager.deleteEpicById(id3);

    }

    private static void printAllTasks(TaskManager manager, HistoryManager historyTask) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getSubtaskByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtask()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : historyTask.getHistory()) {
            System.out.println(task);
        }
    }
}
