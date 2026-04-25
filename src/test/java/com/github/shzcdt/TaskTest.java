package com.github.shzcdt;

import com.github.shzcdt.logic.Epic;
import com.github.shzcdt.logic.Subtask;
import com.github.shzcdt.logic.Task;
import com.github.shzcdt.logic.TaskStatus;
import com.sun.source.tree.NewArrayTree;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 6, 15, 10, 0);
    private static final Duration FIXED_DURATION = Duration.ofHours(2);

    @Test
    @DisplayName("Конструктор сохраняет все поля, включая время")
    void taskConstructorSavesAllFields() {
        Task task = new Task(0, "Test", "Desc", TaskStatus.NEW, FIXED_DURATION, FIXED_TIME);
        task.setId(42);

        assertAll("Task fields",
                () -> assertEquals(42, task.getId()),
                () -> assertEquals("Test", task.getName()),
                () -> assertEquals("Desc", task.getDescription()),
                () -> assertEquals(TaskStatus.NEW, task.getStatus()),
                () -> assertEquals(FIXED_DURATION, task.getDuration()),
                () -> assertEquals(FIXED_TIME, task.getStartTime()),
                () -> assertEquals(FIXED_TIME.plus(FIXED_DURATION), task.getEndTime())
        );
    }

    @Test
    @DisplayName("getEndTime() возвращает null, если нет длительности или старта")
    void getEndTimeReturnsNullWhenTimeMissing() {
        Task noDuration = new Task(1, "Test", "Desc", TaskStatus.NEW, null, FIXED_TIME);
        Task noStartTime = new Task(1, "Test", "Desc", TaskStatus.NEW, FIXED_DURATION, null);

        assertNull(noDuration.getEndTime());
        assertNull(noStartTime.getEndTime());
    }

    @Test
    @DisplayName("equals() и hashCode() работают только по ID")
    void equalsAndHashCodeWorkByIdOnly() {
        Task t1 = new Task(10, "A", "Desc1", TaskStatus.NEW, FIXED_DURATION, FIXED_TIME);
        Task t2 = new Task(10, "B", "Desc2", TaskStatus.DONE, null, null);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("toString() формирует корректный CSV с 8 полями")
    void toStringGeneratesValidCsv() {
        Task task = new Task(5, "Milk", "Store", TaskStatus.NEW, FIXED_DURATION, FIXED_TIME);
        String csv = Task.toString(task);
        assertEquals("5,TASK,Milk,NEW,Store,0,PT2H,2026-06-15 10:00", csv);
    }

    @Test
    @DisplayName("fromString() восстанавливает задачу из CSV")
    void fromStringParsesCorrectly() {
        String csv = "5,TASK,Milk,NEW,Store,0,PT2H,2026-06-15 10:00";
        Task parsed = Task.fromString(csv);

        assertAll("Parsed fields",
                () -> assertEquals(5, parsed.getId()),
                () -> assertEquals("Milk", parsed.getName()),
                () -> assertEquals(TaskStatus.NEW, parsed.getStatus()),
                () -> assertEquals(FIXED_DURATION, parsed.getDuration()),
                () -> assertEquals(FIXED_TIME, parsed.getStartTime())
        );
    }

    @Test
    @DisplayName("fromString() корректно обрабатывает пустые поля времени")
    void fromStringHandlesNullTime() {
        String csv = "7,TASK,NoTime,,None,0, , ";
        Task parsed = Task.fromString(csv);

        assertNull(parsed.getDuration());
        assertNull(parsed.getStartTime());
        assertNull(parsed.getEndTime());
    }

    @Test
    @DisplayName("fromString() кидает исключение при неверном формате")
    void fromStringThrowsOnInvalidFormat() {
        String badCsv = "5,TASK,Milk,NEW,Store"; // Только 5 полей
        assertThrows(IllegalArgumentException.class, () -> Task.fromString(badCsv));
    }

    @Test
    void gettersReturnCorrectValues() {
        Task task = new Task(42, "TestName", "TestDesc", TaskStatus.IN_PROGRESS);

        assertEquals(42, task.getId());
        assertEquals("TestName", task.getName());
        assertEquals("TestDesc", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }


    @Test
    void toStringForTaskProducesValidCsvWithZeroEpicId() {
        Task task = new Task(5, "Buy milk", "Store", TaskStatus.NEW);
        String csv = Task.toString(task);


        assertEquals("5,TASK,Buy milk,NEW,Store,0,,", csv);
    }

    @Test
    void toStringForSubtaskIncludesRealEpicId() {
        Subtask sub = new Subtask(10, "Pack boxes", "Kitchen", TaskStatus.DONE, 5);
        String csv = Task.toString(sub);

        assertEquals("10,SUBTASK,Pack boxes,DONE,Kitchen,5,,", csv);
    }

    @Test
    void toStringForEpicHasZeroEpicId() {
        Epic epic = new Epic(3, "Move", "Relocate apartment", TaskStatus.NEW);
        String csv = Task.toString(epic);

        assertEquals("3,EPIC,Move,NEW,Relocate apartment,0,,", csv);
    }

    @Test
    void fromStringParsesTaskCorrectTypeAndFields() {
        String csv = "5,TASK,Buy milk,NEW,Store,0,,";
        Task result = Task.fromString(csv);

        assertInstanceOf(Task.class, result);

        assertEquals(5, result.getId());
        assertEquals("Buy milk", result.getName());
        assertEquals(TaskStatus.NEW, result.getStatus());
    }

    @Test
    void fromStringParsesSubtaskPreservesEpicLink() {
        String csv = "10,SUBTASK,Pack boxes,IN_PROGRESS,Kitchen,5,PT2H,2024-01-15 10:00";
        Task result = Task.fromString(csv);

        assertInstanceOf(Subtask.class, result);
        assertEquals(10, result.getId());
        assertEquals(5, ((Subtask) result).getEpicId());
    }

    @Test
    void fromStringParsesEpicInstantiatesCorrectly() {
        String csv = "3,EPIC,Move,DONE,Relocate apartment,0,,";
        Task result = Task.fromString(csv);

        assertInstanceOf(Epic.class, result);
        assertEquals(3, result.getId());
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    void fromStringThrowsOnMalformedCsv() {
        String badCsv = "5,TASK,Buy milk,NEW";
        assertThrows(IllegalArgumentException.class, () -> Task.fromString(badCsv));
    }

    @Test
    void fromStringThrowsOnUnknownTaskType() {
        String badTypeCsv = "5,UNKNOWN,Buy milk,NEW,Store,0";
        assertThrows(IllegalArgumentException.class, () -> Task.fromString(badTypeCsv));
    }

    @Test
    void fromStringThrowsOnInvalidEnumValue() {
        String badEnumCsv = "5,TASK,Buy milk,FINISHED,Store,0";
        assertThrows(IllegalArgumentException.class, () -> Task.fromString(badEnumCsv));
    }
}