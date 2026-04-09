package logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
                    + file.getFileName(), exception);
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
                    super.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    super.createSubtask((Subtask) task);
                } else {
                    super.createTask(task);
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

            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(getTasks());
            allTasks.addAll(getSubtasks());
            allTasks.addAll(getEpics());

            allTasks.sort(Comparator.comparing(Task::getId));

            for (Task task : allTasks){
                linesForWriter.add(Task.toString(task));
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
}