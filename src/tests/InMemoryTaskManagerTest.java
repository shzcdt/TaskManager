package tests;

import logic.*;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TasksManagerTest{
    @Override
    protected TaskManager createManager(HistoryManager history) {
        return new InMemoryTaskManager(history);
    }
}
