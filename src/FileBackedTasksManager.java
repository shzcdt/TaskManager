import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final Path file;

    public FileBackedTasksManager(HistoryManager historyManager, Path filePath) {
        super(historyManager);
        this.file = filePath;

        try {
            Files.createDirectories(file.getParent());
        }catch (IOException exception){
            throw new ManagerException("Не удалось создать директорию для файла: "
                    + filePath.getFileName(), exception);
        }

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

    @Override
    public void createTask(Task task){
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task updatedTask){
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId){
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteAllTasks(){
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask){
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllSubtasks(){
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int subtasksId){
        super.deleteSubtaskById(subtasksId);
        save();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask){
        super.updateSubtask(updatedSubtask);
        save();
    }

    @Override
    public void createEpic(Epic epic){
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpic(){
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteEpicById(int epicId){
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic){
        super.updateEpic(updatedEpic);
        save();
    }


    public static void main(String[] args) {
        Path filePath = Paths.get("src/data", "tasks.csv");

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
