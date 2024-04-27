import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class MainHistory {
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
        Long id6 = manager.createSubtask(new Subtask("Нужны гвозди", "купить 300 шт. шурупов 10х150!",
                Task.Status.NEW, epic));
        Epic epic1 = new Epic("Устроиться на работу", "Поиск работы", Task.Status.NEW);
        Long id7 = manager.createEpic(epic1);
        manager.taskById(id1);
        manager.taskById(id1);
        manager.taskById(id2);
        manager.taskById(id3);
        manager.taskById(id4);
        manager.taskById(id5);
        manager.taskById(id6);
        manager.taskById(id7);
        printAllHistory(historyTask);
        manager.delete(id1);
        manager.delete(id7);
        manager.deleteEpicById(id3);
        printAllHistory(historyTask);
    }

    private static void printAllHistory(HistoryManager historyTask) {
        System.out.println("История:");
        for (Task task : historyTask.getHistory()) {
            System.out.println(task);
        }
    }
}

