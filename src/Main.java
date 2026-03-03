import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Тест 2 тех-улучшения");

        manager.createTask(new Task(0, "T1", "", TaskStatus.NEW));
        manager.createEpic(new Epic(0, "E1", "", TaskStatus.NEW));
        manager.createSubtask(new Subtask(0, "S1", "", TaskStatus.NEW, 2));

        manager.getTaskById(1);
        manager.getSubtasksById(3);
        manager.getEpicById(2);

        System.out.println("\n#История");
        System.out.println(manager.history());

        for (int i = 0; i < 8; i++){
            manager.getTaskById(1);
        }
        System.out.println("\n#История");
        System.out.println(manager.history());
    }
}
