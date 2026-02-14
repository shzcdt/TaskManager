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

        Task task1 = new Task(taskManager.generateId(), "Покушать", "Приготовить завтрак", TaskStatus.NEW);

        Task task2 = new Task(taskManager.generateId(), "Погулять", "Пойти погулять на улице", TaskStatus.NEW);

        taskManager.addTask(packBoxes);

        epic.addSubtaskId(packBoxes.getId());
        taskManager.addTask(task1);
        System.out.println(taskManager.getTaskById(3));
        taskManager.updateTask(task2, task1.getId());
        System.out.println(taskManager.getTaskById(4));
        System.out.println(epic);
        System.out.println(packBoxes);
        System.out.println(taskManager.getTaskById(epic.getId()));
        System.out.println(taskManager.getTasks());
        taskManager.removeAllTasks();
        System.out.println(taskManager.getTasks());
    }
}
