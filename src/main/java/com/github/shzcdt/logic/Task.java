package com.github.shzcdt.logic;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Getter
@Setter
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

    private static String timeToString(Object obj) {
        return switch (obj){
            case LocalDateTime date -> date.format(FORMAT);
            case Duration duration -> duration.toString();
            case null -> "";
            default -> throw new IllegalArgumentException("Неподдерживаемый тип времени: " + obj.getClass());
        };
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

        return String.format("%d,%s,%s,%s,%s,%d,%s,%s", task.id, type, task.name, task.status, task.description, epicId,
                timeToString(task.duration), timeToString(task.startTime));
    }


    public static Task fromString(String value) {
        String[] partsOfTask = value.split(",", -1);

        if (partsOfTask.length != 8) {
            throw new IllegalArgumentException("Неверный формат строки задачи");
        }

        int id = Integer.parseInt(partsOfTask[0]);
        String type = partsOfTask[1];
        String name = partsOfTask[2];
        TaskStatus status;
        if (partsOfTask[3].isEmpty() || partsOfTask[3].isBlank()){
            status = null;
        } else {
            status = TaskStatus.valueOf(partsOfTask[3]);
        }
        String description = partsOfTask[4];
        int epicId = Integer.parseInt(partsOfTask[5]);
        Duration duration;
        LocalDateTime startTime;

        if (partsOfTask[6].isEmpty() || partsOfTask[6].isBlank()){
            duration = null;
        } else {
            duration = Duration.parse(partsOfTask[6]);
        }

        if (partsOfTask[7].isEmpty() || partsOfTask[7].isBlank()){
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(partsOfTask[7], FORMAT);
        }

        return switch (type) {
            case "SUBTASK" -> new Subtask(id, name, description, status, epicId, duration, startTime);
            case "EPIC" -> new Epic(id, name, description, status, duration, startTime);
            case "TASK" -> new Task(id, name, description, status, duration, startTime);
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