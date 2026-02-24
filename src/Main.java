import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

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
        System.out.println(manager.getTaskById(11000));*/

        System.out.println("Тест 2 тех-улучшения");
        TaskManager mng = Managers.getDefault();

        mng.createTask(new Task(0, "T1", "", TaskStatus.NEW));
        mng.createEpic(new Epic(0, "E1", "", TaskStatus.NEW));
        mng.createSubtask(new Subtask(0, "S1", "", TaskStatus.NEW, 2));

        mng.getTaskById(1);
        mng.getSubtasksById(3);
        mng.getEpicById(2);

        System.out.println(mng.history());

        for (int i = 0; i < 8; i++){
            mng.getTaskById(1);
        }
        System.out.println(mng.history().size());
        System.out.println(mng.history());
    }
}
