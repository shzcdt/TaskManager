import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final Path file;

    public FileBackedTasksManager(HistoryManager historyManager, String filePath) {
        super(historyManager);
        this.file = Paths.get(filePath);
        load();
    }

    private void load() {
        // прочитать файл и восстановить задачи
        if (!Files.exists(file)){
            return;
        }
    }

    private void save() {

    }


}
