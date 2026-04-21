package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private final List<Integer> subTaskIds;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status){
        super(id, name, description, status);
        subTaskIds = new ArrayList<>();
    }
    public Epic(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime localDateTime){
        super(id, name, description, status, duration, localDateTime);
        subTaskIds = new ArrayList<>();
    }

    public List<Integer> getSubTaskIds() {
        return new ArrayList<>(subTaskIds);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    protected void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void removeSubTaskId(int subtaskId){
        subTaskIds.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public String toString(){
        return String.format("Epic:{id=%d, name='%s', subtasks:%s, status=%s, description='%s'}", getId(),
                getName(), subTaskIds, getStatus(), getDescription());
    }
}
