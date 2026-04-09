package logic;

import java.util.List;

public interface TaskManager {

    List<Task> getTasks();
    Task getTaskById(int taskId);
    void deleteAllTasks();
    void deleteTaskById(int taskId);
    void createTask(Task task);
    void updateTask(Task updatedTask);

    List<Subtask> getSubtasks();
    Subtask getSubtasksById(int subtaskId);
    void deleteAllSubtasks();
    void deleteSubtaskById(int subtasksId);
    void createSubtask(Subtask subtask);
    void updateSubtask(Subtask updatedSubtask);

    List<Epic> getEpics();
    Epic getEpicById(int epicId);
    void deleteAllEpic();
    void deleteEpicById(int epicId);
    void createEpic(Epic epic);
    void updateEpic(Epic updatedEpic);

    List<Subtask> getSubtasksByEpicId(int epicId);
    List<Task> history();
}
