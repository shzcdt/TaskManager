public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Epic epic = new Epic(manager.generateId(), "Переезд", "Организовать переезд",
                TaskStatus.NEW);
        manager.addTask(epic);

        Subtask packBoxes = new Subtask(
                manager.generateId(),
                "Собрать коробки",
                "Упоковать вещи",
                TaskStatus.NEW,
                epic.getId()
        );

        manager.addTask(packBoxes);
        epic.addSubtaskId(packBoxes.getId());

        Subtask sayGoodBye = new Subtask(
                manager.generateId(),
                "Попрощаться",
                "Сказать соседям пока",
                TaskStatus.NEW,
                epic.getId()
        );
        manager.addTask(sayGoodBye);
        epic.addSubtaskId(sayGoodBye.getId());

        System.out.println("=== Проверка обновление Epic ===");
        System.out.println("===После создания===");
        System.out.println(epic);

        packBoxes.setStatus(TaskStatus.DONE);
        manager.updateSubTask(packBoxes);

        System.out.println("\n=== После обновления одной подзадачи на DONE ===");
        System.out.println(manager.getTaskById(epic.getId()));

        sayGoodBye.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sayGoodBye);

        System.out.println("\n=== После обновлнеия второй подзадачи на DONE ===");
        System.out.println(manager.getTaskById(epic.getId()));

        System.out.println("\n=== Проверка обновления задачи ===");

        Task task1 = new Task(manager.generateId(), "Покушать", "Приготовить завтрак", TaskStatus.NEW);

        Task task2 = new Task(manager.generateId(), "Погулять", "Пойти погулять на улице", TaskStatus.NEW);
        manager.addTask(task1);
        System.out.println(manager.getTaskById(task1.getId()));
        manager.updateTask(task2, task1.getId());
        System.out.println(manager.getTaskById(task2.getId()));

        System.out.println("\n=== Прочие проверки === ");

        System.out.println(epic);
        System.out.println(packBoxes);
        System.out.println(manager.getTaskById(epic.getId()));
        System.out.println(manager.getTasks());
        manager.removeAllTasks();
        System.out.println(manager.getTasks());
    }
}
