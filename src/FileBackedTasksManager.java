import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final Path file;

    public FileBackedTasksManager(HistoryManager historyManager, String filePath) {
        super(historyManager);
        this.file = Paths.get(filePath);
        load();
    }

    private void load() {
        if (!Files.exists(file)) {
            System.out.println("Файл не найден");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("id,")) {
                    continue;
                }


                Task task = Task.fromString(line);

                if (task instanceof Epic) {
                    createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    createSubtask((Subtask) task);
                } else {
                    createTask(task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerException("Ошибка при загрузке данных из файла: " + file, exception);
        }
    }

    private void save() {
        if (!Files.exists(file)) {
            System.out.println("Файл не найден");
            return;
        }
        try {
            List<String> linesForWriter = new ArrayList<>();
            String heading = "id,type,name,status,description,epicId";

            linesForWriter.add(heading);

            List<Task> tasks = getTasks();
            List<Subtask> subtasks = getSubtasks();
            List<Epic> epics = getEpics();

            for (Epic epic : epics) {
                linesForWriter.add(Task.toString(epic));
            }

            for (Task task : tasks) {
                linesForWriter.add(Task.toString(task));
            }

            for (Subtask subtask : subtasks) {
                linesForWriter.add(Task.toString(subtask));
            }

            Files.write(file, linesForWriter, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new ManagerException("Ошибка при записи в файл: " + file, exception);
        }
    }

    public static void main(String[] args) {
        String filePath = Paths.get("").toAbsolutePath().resolve("data/tasks.csv").toString();

        System.out.println(filePath);
        FileBackedTasksManager manager = new FileBackedTasksManager(
                Managers.getDefaultHistory(),
                filePath
        );

        System.out.println("---Создаем задачи---");
        Task task = new Task(0, "Купить молоко", "В магазине", TaskStatus.NEW);
        manager.createTask(task);

        Epic epic = new Epic(0, "Переезд", "Организовать переезд", TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(0, "Собрать коробки", "Упаковать вещи", TaskStatus.IN_PROGRESS, epic.getId());
        manager.createSubtask(subtask);
        manager.save();

        System.out.println("Задачи после создания:");
        manager.getTasks().forEach(System.out::println);
        manager.getEpics().forEach(System.out::println);
        manager.getSubtasks().forEach(System.out::println);

        System.out.println("\nФайл 'data/tasks.csv' должен быть создан.");
        System.out.println("Запусти программу ещё раз, чтобы проверить загрузку!");
    }
}
