import model.Epic;
import model.Subtask;
import model.Task;
import service.Manager;
public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Long id1 = manager.createTask(new Task("Поход в магазин", "Купить хлеб", Task.Status.NEW));
        Long id2 = manager.createTask(new Task("Поход в гараж", "Зарядить аккумулятор",
                Task.Status.NEW));
        Long id3 = manager.createEpic(new Epic("Ремонт квартиры", "Необходимо приобрести",
                Task.Status.NEW));
        Long id4 = manager.createSubtask(new Subtask("Нужен цемент", "купить 30 мешков цемента по 25 кг!",
                Task.Status.NEW, manager.getEpicById(id3)));
        Long id5 = manager.createSubtask(new Subtask("Нужны шурупы", "купить 150 шт. шурупов М8х100!",
                Task.Status.NEW, manager.getEpicById(id3)));
        Long id6 = manager.createEpic(new Epic("Устроиться на работу", "Поиск работы", Task.Status.NEW));
        Long id7 = manager.createSubtask(new Subtask("Нужен резюме", "Найти шаблон резюме!",
                Task.Status.NEW, manager.getEpicById(id6)));
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtask());
        manager.updateTask(new Task(id1, "Поход в магазин", "Купить хлеб", Task.Status.IN_PROGRESS));
        manager.updateTask(new Task(id2, "Поход в гараж", "Зарядить аккумулятор", Task.Status.DONE));
        manager.updateSubtask(new Subtask(id4, "Нужен цемент", "купить 30 мешков цемента по 25 кг!",
                Task.Status.DONE, manager.getEpicById(id3)));
        manager.updateSubtask(new Subtask(id5, "Нужны шурупы", "купить 150 шт. шурупов М8х100!",
                Task.Status.DONE, manager.getEpicById(id3)));
        manager.updateSubtask(new Subtask(id7, "Нужен резюме", "Найти шаблон резюме!",
                Task.Status.IN_PROGRESS, manager.getEpicById(id6)));
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtask());
        manager.delete(id1);
        manager.deleteEpicById(id3);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtask());
    }
}
