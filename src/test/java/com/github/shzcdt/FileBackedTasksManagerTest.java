package com.github.shzcdt;

import com.github.shzcdt.logic.FileBackedTasksManager;
import com.github.shzcdt.logic.HistoryManager;
import com.github.shzcdt.logic.TaskManager;
import java.nio.file.Paths;

public class FileBackedTasksManagerTest extends TasksManagerTest{

    @Override
    protected TaskManager createManager(HistoryManager history) {
        return new FileBackedTasksManager(history, Paths.get("src/tests/test.csv"));
    }
}
