public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task simple = new Task(0, "Купить молоко", "В магазине за углом", TaskStatus.NEW);
        manager.createTask(simple);

        Epic move = new Epic(0, "Переезд", "Организовать переезд", TaskStatus.NEW);
        manager.createEpic(move);

        Subtask pack = new Subtask(0, "Собрать коробки", "...", TaskStatus.NEW, move.getId());
        manager.createSubtask(pack);

        System.out.println("Tasks: " + manager.getTasks());
        System.out.println("Subtasks: " + manager.getSubtask());
        System.out.println("Epics: " + manager.getEpics());
        // Обновим подзадачу
        pack.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(pack);
        System.out.println("ТЫ лох ебанный коддинга, если тут не process");
        System.out.println("Epic after update: " + manager.getEpicById(move.getId()));

        // Удаляем подзадачу
        manager.deleteSubtaskById(pack.getId());
        System.out.println("Epic after delete: " + manager.getEpicById(move.getId()));
    }
}
