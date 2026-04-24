package com.github.shzcdt;

import com.github.shzcdt.logic.Epic;
import com.github.shzcdt.logic.Subtask;
import com.github.shzcdt.logic.Task;
import com.github.shzcdt.logic.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

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