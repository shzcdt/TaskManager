import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager;

    private int nextId = 1;

    public InMemoryTaskManager() {
        this.historyManager = new InMemoryHistoryManager();
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }


    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));

        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));

        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));

        return epics.get(epicId);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
    }

    @Override
    public void deleteAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(int subtasksId) {
        Subtask subtask = subtasks.get(subtasksId);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());

            subtasks.remove(subtasksId);
            if (epic != null) {
                epic.removeSubTaskId(subtasksId);
                updateEpicStatus(epic.getId());
            }
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        epics.remove(epicId);

        subtasks.entrySet().removeIf(entry -> entry.getValue().getEpicId() == epicId);
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            updateEpicStatus(updatedSubtask.getEpicId());
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        Epic existing = epics.get(updatedEpic.getId());
        if (existing != null) {
            existing.setName(updatedEpic.getName());
            existing.setDescription(updatedEpic.getDescription());
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;


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

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return subtasks.values().stream()
                .filter(s -> s.getEpicId() == epicId)
                .collect(Collectors.toList());
    }


    private int generateId() {
        return nextId++;
    }
}
