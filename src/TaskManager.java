import java.util.HashMap;
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
}
