package tests;

import logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    private Task createTask(int id) {
        return new Task(id, "Task-" + id, "desc", TaskStatus.NEW);
    }

    private List<Integer> getHistoryIds() {
        return historyManager.getHistory().stream()
                .map(Task::getId)
                .toList();
    }

    @Test
    void getHistoryEmptyReturnEmptyList() {
        assertTrue(historyManager.getHistory().isEmpty(), "Истрия пуста при создании");
    }

    @Test
    void addSingleTaskAddsToHistory() {
        historyManager.add(createTask(1));
        assertEquals(List.of(1), getHistoryIds());
    }

    @Test
    void addMultipleTasksPreservesInsertionsOrder() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        assertEquals(List.of(1, 2, 3), getHistoryIds());
    }

    @Test
    void addDuplicateTaskMovesToLastAndRemovesPrevious() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));

        historyManager.add(createTask(1));

        assertEquals(List.of(2, 3, 1), getHistoryIds());
        assertEquals(3, historyManager.getHistory().size(), "Размер истории не расти при дублях");
    }

    @Test
    void addExceedsMaxSizeEvictsOldest() {
        for (int i = 0; i <= 11; i++) {
            historyManager.add(createTask(i));
        }

        assertEquals(10, historyManager.getHistory().size());
        assertEquals(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11), getHistoryIds());
    }

    @Test
    void removeFromHeadUpdatesCorrectly() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));

        historyManager.remove(1);
        assertEquals(List.of(2, 3), getHistoryIds());
    }

    @Test
    void removeFromMiddleUpdatesCorrectly() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));

        historyManager.remove(2);
        assertEquals(List.of(1, 3), getHistoryIds());
    }

    @Test
    void removeFromTailUpdatesCorrectly() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));

        historyManager.remove(3);
        assertEquals(List.of(1, 2), getHistoryIds());
    }

    @Test
    void combinedOperations() {
        for (int i = 1; i <= 5; i++) historyManager.add(createTask(i));

        historyManager.add(createTask(3));
        assertEquals(List.of(1, 2, 4, 5, 3), getHistoryIds());

        historyManager.remove(2);
        assertEquals(List.of(1, 4, 5, 3), getHistoryIds());

        historyManager.add(createTask(6));
        assertEquals(List.of(1, 4, 5, 3, 6), getHistoryIds());
    }
}