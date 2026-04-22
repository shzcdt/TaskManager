package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private final HistoryManager historyManager;

    private static final Comparator<Task> taskComparator = Comparator.comparing(
            Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())
    ).thenComparingInt(Task::getId);
    private int nextId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null){
            historyManager.add(task);
        }

        return task;
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null){
            historyManager.add(subtask);
        }

        return subtask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null){
            historyManager.add(epic);
        }

        return epic;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getClass().equals(Task.class));
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        prioritizedTasks.removeIf(subTask -> subTask instanceof Subtask);
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
        Task task = tasks.get(taskId);
        tasks.remove(taskId);
        prioritizedTasks.remove(task);
    }

    @Override
    public void deleteSubtaskById(int subtasksId) {
        Subtask subtask = subtasks.get(subtasksId);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());

            subtasks.remove(subtasksId);
            prioritizedTasks.remove(subtask);
            if (epic != null) {
                epic.removeSubTaskId(subtasksId);
                updateEpicStatus(epic.getId());
                recalculateEpicTime(epic.getId());
            }
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = new ArrayList<>(epic.getSubTaskIds());
        epics.remove(epicId);

        for (int subtaskId : subtaskIds){
            deleteSubtaskById(subtaskId);
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
            recalculateEpicTime(epic.getId());
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
            Task oldTask = tasks.get(updatedTask.getId());
            prioritizedTasks.remove(oldTask);
            tasks.put(updatedTask.getId(), updatedTask);
            prioritizedTasks.add(updatedTask);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Subtask oldSubtask = subtasks.get(updatedSubtask.getId());
            prioritizedTasks.remove(oldSubtask);
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            prioritizedTasks.add(updatedSubtask);

            updateEpicStatus(updatedSubtask.getEpicId());
            recalculateEpicTime(updatedSubtask.getEpicId());
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())){
            epics.put(updatedEpic.getId(), updatedEpic);
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

    private void recalculateEpicTime(int epicId){
        Epic epic = epics.get(epicId);
        List<Subtask> subs = getSubtasksByEpicId(epicId);
        if (subs.isEmpty()){
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }

        Duration sumDuration = Duration.ZERO;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        for (Subtask subtask : subs){
            LocalDateTime subStart = subtask.getStartTime();
            LocalDateTime subEnd = subtask.getEndTime();
            Duration subDuration = subtask.getDuration();

            if (subStart == null || subDuration == null) continue;

            sumDuration = sumDuration.plus(subDuration);

            if (startTime == null || subStart.isBefore(startTime)){
                startTime = subStart;
            }
            if (endTime == null || subEnd.isAfter(endTime)){
                endTime = subEnd;
            }
        }

        epic.setStartTime(startTime);
        epic.setDuration(sumDuration);
        epic.setEndTime(endTime);
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
