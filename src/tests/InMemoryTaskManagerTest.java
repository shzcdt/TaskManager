package tests;

import logic.HistoryManager;
import logic.InMemoryTaskManager;
import logic.TaskManager;

public class InMemoryTaskManagerTest extends TasksManagerTest{
    @Override
    protected TaskManager createManager(HistoryManager history) {
        return new InMemoryTaskManager(history);
    }
}
