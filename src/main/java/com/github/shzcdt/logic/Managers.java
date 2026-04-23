package com.github.shzcdt.logic;

import java.nio.file.Paths;

public class Managers {
    static HistoryManager sharedHistory = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(sharedHistory);
    }

    public static HistoryManager getDefaultHistory() {
        return sharedHistory;
    }

    public static TaskManager getFileBacked(String pathFile) {
        return new FileBackedTasksManager(sharedHistory, Paths.get(pathFile));
    }
}
