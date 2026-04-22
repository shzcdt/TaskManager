package tests;

import logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EpicTest {
    private TaskManager manager;

    private Epic createEpic(int id) {
        return new Epic(id, "Epic-" + id, "desc", TaskStatus.NEW);
    }

    private Subtask createSubtask(int id, int epicId, Duration duration, LocalDateTime startTime) {
        return new Subtask(id, "Epic-" + id, "desc", TaskStatus.NEW, epicId, duration, startTime);
    }
    private Subtask createSubtask(int id, int epicId) {
        return new Subtask(id, "Epic-" + id, "desc", TaskStatus.NEW, epicId);
    }
    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    public void epicStatusMustBeNewWhenNoSubtasks() {
        Epic epic = createEpic(1);
        manager.createEpic(epic);


        TaskStatus expectedStatus = TaskStatus.NEW;
        TaskStatus actualStatus = epic.getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void epicStatusMustBeInProgressWhenSubtasksInProcess() {
        Epic epic = createEpic(1);
        Subtask subtask = new Subtask(2, "SubtaskForEpic1", "", TaskStatus.IN_PROGRESS, 1);

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

    @Test
    public void epicDurationMustBeSumDurationAboutSubtasks() {
        Duration subsDuration1 = Duration.ofHours(5);
        Duration subsDuration2 = Duration.ofHours(2);
        Duration subsDuration3 = Duration.ofHours(3);

        LocalDateTime startTime1 = LocalDateTime.of(2026, 4, 22, 5, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2026, 4, 22, 12, 0, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2026, 4, 22, 18, 0, 0);

        Epic epic = new Epic(0, "Epic1", "", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(0, "SubtaskForEpic1", "", TaskStatus.IN_PROGRESS, 1, subsDuration1, startTime1);
        Subtask subtask2 = new Subtask(0, "SubtaskForEpic2", "", TaskStatus.IN_PROGRESS, 1, subsDuration2, startTime2);
        Subtask subtask3 = new Subtask(0, "SubtaskForEpic3", "", TaskStatus.IN_PROGRESS, 1, subsDuration3, startTime3);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        LocalDateTime endTime = LocalDateTime.of(2026, 4, 22, 21, 0, 0);
        Duration duration = Duration.ofHours(10);
        assertEquals(endTime, epic.getEndTime(), "Время окончания Epic-задачи");
        assertEquals(duration, epic.getDuration(), "Продолжительность Epic-задачи");
    }

    @Test
    public void epicTimeIsNullWhenNoSubtasks() {
        Epic epic = createEpic(1);

        manager.createEpic(epic);

        assertNull(epic.getDuration());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }

    @Test
    public void epicTimeMatchesSingleSubtask() {
        Duration subDuration = Duration.ofHours(3);
        LocalDateTime subStartTime = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(12, 0));

        Epic epic = createEpic(1);
        Subtask subtask = createSubtask(2, 1, subDuration, subStartTime);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        LocalDateTime expectedEndTime = subStartTime.plus(subDuration);


        assertEquals(subDuration, epic.getDuration());
        assertEquals(subStartTime, epic.getStartTime());
        assertEquals(expectedEndTime, epic.getEndTime());
    }

    @Test
    public void epicTimeAggregatesSequentialSubtasks() {
        Duration subDuration1 = Duration.ofHours(1);
        Duration subDuration2 = Duration.ofHours(1);
        LocalDateTime subStartTime1 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(10, 0));
        LocalDateTime subStartTime2 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(11, 0));

        Epic epic = createEpic(1);
        Subtask subtask1 = createSubtask(2, 1, subDuration1, subStartTime1);
        Subtask subtask2 = createSubtask(3, 1, subDuration2, subStartTime2);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Duration expectedDuration = subDuration1.plus(subDuration2);
        LocalDateTime expectedEndTime = subStartTime2.plus(subDuration2);

        assertEquals(expectedDuration, epic.getDuration());
        assertEquals(subStartTime1, epic.getStartTime());
        assertEquals(expectedEndTime, epic.getEndTime());
    }

    @Test
    public void epicTimeHandlesOverlappingSubtasks() {
        Duration subDuration1 = Duration.ofHours(2);
        Duration subDuration2 = Duration.ofHours(1);
        LocalDateTime subStartTime1 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(10, 0));
        LocalDateTime subStartTime2 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(11, 0));

        Epic epic = createEpic(1);
        Subtask subtask1 = createSubtask(2, 1, subDuration1, subStartTime1);
        Subtask subtask2 = createSubtask(3, 1, subDuration2, subStartTime2);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Duration expectedDuration = subDuration1.plus(subDuration2);
        LocalDateTime expectedEndTime = subStartTime2.plus(subDuration2);

        assertEquals(expectedDuration, epic.getDuration());
        assertEquals(subStartTime1, epic.getStartTime());
        assertEquals(expectedEndTime, epic.getEndTime());
    }

    @Test
    public void epicTimeIgnoresSubtasksWithNullTime() {
        Duration subDuration1 = Duration.ofHours(2);
        LocalDateTime subStartTime1 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(10, 0));

        Epic epic = createEpic(1);
        Subtask subtask1 = createSubtask(2, 1, subDuration1, subStartTime1);
        Subtask subtask2 = createSubtask(3, 1);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);



        assertEquals(subDuration1, epic.getDuration());
        assertEquals(subStartTime1, epic.getStartTime());
        assertEquals(subtask1.getEndTime(), epic.getEndTime());
    }

    @Test
    public void epicTimeRecalculatesAfterSubtaskUpdate() {
        Duration subDuration1 = Duration.ofHours(2);
        Duration subDuration2 = Duration.ofHours(1);
        LocalDateTime subStartTime1 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(10, 0));
        LocalDateTime subStartTime2 = LocalDateTime.of(LocalDate.of(2026, 4, 22), LocalTime.of(11, 0));

        Epic epic = createEpic(1);
        Subtask subtask1 = createSubtask(2, 1, subDuration1, subStartTime1);
        Subtask subtask2 = createSubtask(3, 1, subDuration2, subStartTime2);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Subtask updatedSubtask2 = createSubtask(3, 1, Duration.ofHours(3), subStartTime2);

        manager.updateSubtask(updatedSubtask2);

        Duration expectedDuration = subDuration1.plus(updatedSubtask2.getDuration());

        assertEquals(expectedDuration, epic.getDuration());
        assertEquals(subStartTime1, epic.getStartTime());
        assertEquals(updatedSubtask2.getEndTime(), epic.getEndTime());
    }
}