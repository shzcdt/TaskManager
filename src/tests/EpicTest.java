package tests;

import logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private TaskManager manager;


    @BeforeEach
    public void createManger() {
        manager = Managers.getDefault();
    }

    // Проверить расчёт статуса эпика, когда у него пустой список подзадач.
    @Test
    public void epicStatusMustBeNewWhenNoSubtasks() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        manager.createEpic(epic);


        TaskStatus expectedStatus = TaskStatus.NEW;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    // Должен быть в процессе при не готовом сабтаске и готовым при всех готовых сабтасках
    @Test
    public void epicStatusMustBeInProgressWhenSubtasksInProcess() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        Subtask subtask = new Subtask(1, "SubtaskForEpic1", "", TaskStatus.IN_PROGRESS, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        TaskStatus expectedStatus = TaskStatus.IN_PROGRESS;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void epicStatusMustBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.IN_PROGRESS);
        Subtask subtask1 = new Subtask(0, "SubtaskForEpic1", "", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(0, "SubtaskForEpic2", "", TaskStatus.NEW, 1);
        Subtask subtask3 = new Subtask(0, "SubtaskForEpic3", "", TaskStatus.NEW, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        TaskStatus expectedStatus = TaskStatus.NEW;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void epicStatusMustBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.IN_PROGRESS);
        Subtask subtask1 = new Subtask(0, "SubtaskForEpic1", "", TaskStatus.DONE, 1);
        Subtask subtask2 = new Subtask(0, "SubtaskForEpic2", "", TaskStatus.DONE, 1);
        Subtask subtask3 = new Subtask(0, "SubtaskForEpic3", "", TaskStatus.DONE, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        TaskStatus expectedStatus = TaskStatus.DONE;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void epicStatusMustBeInProgressWhenSubtasksDoneAndNew() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(0, "SubtaskForEpic1", "", TaskStatus.DONE, 1);
        Subtask subtask2 = new Subtask(0, "SubtaskForEpic2", "", TaskStatus.IN_PROGRESS, 1);
        Subtask subtask3 = new Subtask(0, "SubtaskForEpic3", "", TaskStatus.DONE, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        TaskStatus expectedStatus = TaskStatus.IN_PROGRESS;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void epicStatusMustBeInProgressWhenAllSubtasksInProgress() {
        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(0, "SubtaskForEpic1", "", TaskStatus.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask(0, "SubtaskForEpic2", "", TaskStatus.IN_PROGRESS, 1);
        Subtask subtask3 = new Subtask(0, "SubtaskForEpic3", "", TaskStatus.IN_PROGRESS, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        TaskStatus expectedStatus = TaskStatus.IN_PROGRESS;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }
}