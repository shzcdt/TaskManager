package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Duration duration;
    private LocalDateTime startTime;

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this(id, name, description, status);
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    protected void setId(int newId){
        id = newId;
    }

    protected void setStatus(TaskStatus newStatus){
        status = newStatus;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public static String toString(Task task) {
        String type;
        int epicId = 0;

        if (task instanceof Subtask) {
            type = "SUBTASK";
            epicId = ((Subtask) task).getEpicId();
        } else if (task instanceof Epic) {
            type = "EPIC";
        } else {
            type = "TASK";
        }

        return String.format("%d,%s,%s,%s,%s,%d", task.id, type, task.name, task.status, task.description, epicId);
    }


    public static Task fromString(String value) {
        String[] partsOfTask = value.split(",");

        if (partsOfTask.length != 6) {
            throw new IllegalArgumentException("Неверный формат строки задачи");
        }

        int id = Integer.parseInt(partsOfTask[0]);
        String type = partsOfTask[1];
        String name = partsOfTask[2];
        TaskStatus status = TaskStatus.valueOf(partsOfTask[3]);
        String description = partsOfTask[4];
        int epicId = Integer.parseInt(partsOfTask[5]);

        return switch (type) {
            case "SUBTASK" -> new Subtask(id, name, description, status, epicId);
            case "EPIC" -> new Epic(id, name, description, status);
            case "TASK" -> new Task(id, name, description, status);
            default -> throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        };
    }

    @Override
    public String toString(){
        return String.format("Task:{id=%d, name='%s', status=%s, description='%s', duration='%s', startTime='%s', endTime='%s'}",
                id, name, status, description, timeToString(duration), timeToString(startTime), timeToString(getEndTime()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Task) && this.id == ((Task) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}