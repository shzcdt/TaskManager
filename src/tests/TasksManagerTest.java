package tests;

import logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TasksManagerTest<T extends TaskManager> {
    protected abstract T createManager(HistoryManager history);

    private T manager;

    @BeforeEach
    void setUp() {
        HistoryManager history = new InMemoryHistoryManager();
        manager = createManager(history);
    }

    @AfterEach
    void tearDown() {
        if (manager != null) {
            manager.deleteAllTasks();
            manager.deleteAllSubtasks();
            manager.deleteAllEpic();
        }
    }

    @Test
    void getTasksEmptyReturnsEmptyList() {
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void createAndGetTaskStandardReturnsCreatedTask() {
        Task task = new Task(0, "Task1", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        int id = task.getId();

        Task actual = manager.getTaskById(id);
        assertNotNull(actual);
        assertEquals(task, actual);
    }

    @Test
    void getTaskByIdNonExistentReturnsNull() {
        assertNull(manager.getTaskById(999));
    }

    @Test
    void updateTaskStandardUpdatesFields() {
        Task task = new Task(0, "Old", "Desc", TaskStatus.NEW);
        manager.createTask(task);
        int id = task.getId();

        Task updated = new Task(id, "NewName", "NewDesc", TaskStatus.IN_PROGRESS);
        manager.updateTask(updated);

        Task actual = manager.getTaskById(id);
        assertEquals("NewName", actual.getName());
        assertEquals("NewDesc", actual.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, actual.getStatus());
    }

    @Test
    void updateTaskNonExistentIgnored() {
        Task updated = new Task(999, "New", "", TaskStatus.NEW);
        manager.updateTask(updated);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void deleteTaskByIdStandardRemovesTask() {
        Task task = new Task(0, "Task1", "", TaskStatus.NEW);
        manager.createTask(task);
        int id = task.getId();

        manager.deleteTaskById(id);
        assertNull(manager.getTaskById(id));
    }

    @Test
    void deleteTaskByIdNonExistentDoesNotThrow() {
        assertDoesNotThrow(() -> manager.deleteTaskById(999));
    }

    @Test
    void getEpicsEmptyReturnsEmptyList() {
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void createAndGetEpicStandardReturnsCreated() {
        Epic epic = new Epic(0, "Epic1", "Desc", TaskStatus.NEW);
        manager.createEpic(epic);
        int id = epic.getId();

        assertEquals(epic, manager.getEpicById(id));
    }

    @Test
    void updateEpicStandardUpdatesNameAndDesc() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic);
        int id = epic.getId();

        Epic updated = new Epic(id, "Updated", "NewDesc", TaskStatus.IN_PROGRESS);
        manager.updateEpic(updated);

        Epic found = manager.getEpicById(id);
        assertEquals("Updated", found.getName());
        assertEquals("NewDesc", found.getDescription());
    }

    @Test
    void deleteEpicByIdStandardRemovesEpicAndSubtasks() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic);
        int epicId = epic.getId();

        Subtask sub = new Subtask(0, "Sub1", "", TaskStatus.NEW, epicId);
        manager.createSubtask(sub);
        int subId = sub.getId();

        manager.deleteEpicById(epicId);

        assertNull(manager.getEpicById(epicId));
        assertNull(manager.getSubtasksById(subId));
    }

    @Test
    void getSubtasksEmptyReturnsEmptyList() {
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void createSubtaskWithExistingEpicLinksCorrectly() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic);
        int epicId = epic.getId();

        Subtask sub = new Subtask(0, "Sub1", "", TaskStatus.NEW, epicId);
        manager.createSubtask(sub);
        int subId = sub.getId();

        assertEquals(sub, manager.getSubtasksById(subId));
        assertEquals(1, manager.getSubtasksByEpicId(epicId).size());
    }

    @Test
    void createSubtaskNonExistentEpicIdDoesNotCrash() {
        Subtask sub = new Subtask(0, "Sub1", "", TaskStatus.NEW, 999);

        assertDoesNotThrow(() -> manager.createSubtask(sub));

        assertNotNull(manager.getSubtasksById(sub.getId()));

        assertEquals(1, manager.getSubtasksByEpicId(999).size());
    }

    @Test
    void getSubtasksByEpicIdNonExistentEpicReturnsEmptyList() {
        assertTrue(manager.getSubtasksByEpicId(999).isEmpty());
    }

    @Test
    void history_addsTaskOnGetById() {
        Task task = new Task(0, "Task1", "", TaskStatus.NEW);
        manager.createTask(task);
        manager.getTaskById(task.getId());

        List<Task> history = manager.history();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}