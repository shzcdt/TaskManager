package tests;

import logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TasksManagerTest<T extends TaskManager> {
    protected abstract T createManager(HistoryManager history);

    private T manager;

    @BeforeEach
    public void createNewManager(){
        HistoryManager history = new InMemoryHistoryManager();
        manager = createManager(history);
    }
    @Test
    public void getTasksTest() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        Task task2 = new Task(2, "Task2", "", TaskStatus.NEW);
        Task task3 = new Task(3, "Task3", "", TaskStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(task1);
        expectedTasks.add(task2);
        expectedTasks.add(task3);

        List<Task> actualTasks = manager.getTasks();

        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void getTaskByIdTest() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        Task task2 = new Task(2, "Task2", "", TaskStatus.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        Task expectedTask = task2;
        Task actualTask = manager.getTaskById(2);

        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void deleteAllTasksTest() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        Task task2 = new Task(2, "Task2", "", TaskStatus.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        manager.deleteAllTasks();

        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    public void deleteTaskById() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        Task task2 = new Task(2, "Task2", "", TaskStatus.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        manager.deleteTaskById(2);

        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(task1);

        List<Task> actualTasks = manager.getTasks();

        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void createTask() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        manager.createTask(task1);

        assertEquals(task1, manager.getTaskById(1));
    }

    @Test
    public void updateTask() {
        Task task1 = new Task(1, "Task1", "", TaskStatus.NEW);
        manager.createTask(task1);
        Task updateTask = new Task(1, "Update", "", TaskStatus.NEW);
        manager.updateTask(updateTask);

        Task expectedTask = updateTask;

        Task actualTask = manager.getTaskById(1);

        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void getSubtasksTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(2, "subtask2", "", TaskStatus.NEW, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        List<Subtask> expectedSubtasks = new ArrayList<>();
        expectedSubtasks.add(subtask1);
        expectedSubtasks.add(subtask2);

        assertEquals(expectedSubtasks, manager.getSubtasks());
    }

    @Test
    public void getSubtaskByIdTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(2, "subtask2", "", TaskStatus.NEW, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(subtask2, manager.getSubtasksById(2));
    }

    @Test
    public void deleteAllSubtasksTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(2, "subtask2", "", TaskStatus.NEW, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.deleteAllSubtasks();

        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void deleteSubtasksByIdTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(2, "subtask2", "", TaskStatus.NEW, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.deleteSubtaskById(1);
        List<Subtask> expectedSubtasks = new ArrayList<>();
        expectedSubtasks.add(subtask2);

        assertEquals(expectedSubtasks, manager.getSubtasks());
    }

    @Test
    public void createSubtasksTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        manager.createSubtask(subtask1);

        assertEquals(subtask1, manager.getSubtasksById(1));
    }

    @Test
    public void updateSubtaskTest() {
        Subtask subtask1 = new Subtask(1, "subtask1", "", TaskStatus.NEW, 1);
        manager.createSubtask(subtask1);
        Subtask update = new Subtask(1, "Update", "", TaskStatus.NEW, 1);
        manager.updateSubtask(update);

        assertEquals(update, manager.getSubtasksById(1));
    }

    @Test
    public void getEpicsTest(){
        Epic epic1 = new Epic(1, "Epic1", "", TaskStatus.NEW);
        Epic epic2 = new Epic(2, "Epic2", "", TaskStatus.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        List<Epic> expectedEpics = new ArrayList<>();
        expectedEpics.add(epic1);
        expectedEpics.add(epic2);

        assertEquals(expectedEpics, manager.getEpics());
    }

    @Test
    public void getEpicByIdTest() {
        Epic epic1 = new Epic(1, "epic1", "", TaskStatus.NEW);
        Epic epic2 = new Epic(2, "Epic2", "", TaskStatus.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Epic expectedEpic = epic2;


        assertEquals(expectedEpic, manager.getEpicById(2));
    }

    @Test
    public void deleteAllEpicTest(){
        Epic epic1 = new Epic(1, "Epic1", "", TaskStatus.NEW);
        Epic epic2 = new Epic(2, "Epic2", "", TaskStatus.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        manager.deleteAllEpic();

        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    public void deleteEpicByIdTest(){
        Epic epic1 = new Epic(1, "epic1", "", TaskStatus.NEW);
        Epic epic2 = new Epic(2, "Epic2", "", TaskStatus.NEW);

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        manager.deleteEpicById(1);

        List<Epic> expectedEpics = new ArrayList<>();
        expectedEpics.add(epic2);

        assertEquals(expectedEpics, manager.getEpics());
    }

    @Test
    public void createEpicTest(){
        Epic epic1 = new Epic(1, "Epic1", "", TaskStatus.NEW);

        manager.createEpic(epic1);

        assertEquals(epic1, manager.getEpicById(1));
    }

    @Test
    public void updateEpicTest(){
        Epic epic1 = new Epic(1, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic1);

        Epic update = new Epic(1, "Update", "", TaskStatus.IN_PROGRESS);
        manager.updateEpic(update);

        assertEquals(update, manager.getEpicById(1));
    }

    @Test
    public void getSubtasksByEpicIdTest(){
        Epic epic1 = new Epic(1, "epic1", "", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(2, "subtask1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(3, "subtask2", "", TaskStatus.NEW, 1);

        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        List<Subtask> expectedSubtasks = new ArrayList<>();
        expectedSubtasks.add(subtask1);
        expectedSubtasks.add(subtask2);

        assertEquals(expectedSubtasks, manager.getSubtasksByEpicId(1));
    }

    @Test
    public void getHistoryTest() {
        Epic epic = new Epic(1, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic);
        manager.getEpicById(1);

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic);

        List<Task> actualHistory = manager.history();

        assertEquals(expectedHistory, actualHistory);
    }

    @AfterEach
    public void deleteTestData() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpic();
    }
}