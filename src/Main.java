public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic = new Epic(taskManager.generateId(), "Переезд", "Организовать переезд",
                TaskStatus.NEW);
        taskManager.addTask(epic);

        Subtask packBoxes = new Subtask(
                taskManager.generateId(),
                "Собрать коробки",
                "Упоковать вещи в коробки",
                TaskStatus.NEW,
                epic.getId()
        );
        taskManager.addTask(packBoxes);

        epic.addSubtaskId(packBoxes.getId());

        System.out.println(epic);
        System.out.println(packBoxes);
        System.out.println(taskManager.getTaskById(epic.getId()));
        System.out.println(taskManager.getTasks());
        taskManager.removeAllTasks();
        System.out.println(taskManager.getTasks());
    }
}
