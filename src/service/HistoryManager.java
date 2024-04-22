package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);//должен помечать задачи как просмотренные

    void remove(int id);//добавить метод для удаления из истории!
    List<Task> getHistory();//возвращать их список
}
