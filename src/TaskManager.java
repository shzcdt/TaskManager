import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private int nextId = 1;

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Task getTaskById(int taskId){
        return tasks.get(taskId);
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                if (subtask.getEpicId() == epicId) {
                    result.add(subtask);
                }
            }
        }
        return result;
    }



    public void addTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task, int taskId){
        tasks.remove(taskId);
        tasks.put(task.getId(), task);
    }

    public int generateId(){
        return nextId++;
    }


    public void removeAllTasks(){
        tasks.clear();
    }

    public void removeTaskById(int taskId){
        tasks.remove(taskId);
    }


    public void updateSubtask(Subtask updatedSubtask){
        tasks.put(updatedSubtask.getId(), updatedSubtask);

        int epicId = updatedSubtask.getEpicId();
        Task epicTask = tasks.get(epicId);

        if (epicTask instanceof Epic) {
            Epic epic = (Epic) epicTask;

            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = getSubtasksByEpicId(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != TaskStatus.NEW) allNew = false;
            if (subtask.getStatus() != TaskStatus.DONE) allDone = false;
        }

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
