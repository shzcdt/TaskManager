package tests;

import logic.FileBackedTasksManager;
import logic.HistoryManager;
import logic.TaskManager;

import java.nio.file.Paths;

public class FileBackedTasksManagerTest extends TasksManagerTest{

    @Override
    protected TaskManager createManager(HistoryManager history) {
        return new FileBackedTasksManager(history, Paths.get("src/tests/test.csv"));
    }
}
