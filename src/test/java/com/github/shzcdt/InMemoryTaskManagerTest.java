package com.github.shzcdt;

import com.github.shzcdt.logic.HistoryManager;
import com.github.shzcdt.logic.InMemoryTaskManager;
import com.github.shzcdt.logic.TaskManager;

public class InMemoryTaskManagerTest extends TasksManagerTest{
    @Override
    protected TaskManager createManager(HistoryManager history) {
        return new InMemoryTaskManager(history);
    }
}
