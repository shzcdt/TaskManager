package logic;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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
        return String.format("Task:{id=%d, name='%s', status=%s, description='%s'}", id, name, status, description);
    }
}