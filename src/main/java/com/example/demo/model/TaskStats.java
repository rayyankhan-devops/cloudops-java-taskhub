package com.example.demo.model;

import java.util.Map;

public class TaskStats {
    private long totalTasks;
    private long todoCount;
    private long inProgressCount;
    private long doneCount;
    private long criticalCount;
    private long highCount;
    private Map<String, Long> tagCounts;

    public TaskStats() {
    }

    public TaskStats(long totalTasks, long todoCount, long inProgressCount, long doneCount, long criticalCount, long highCount, Map<String, Long> tagCounts) {
        this.totalTasks = totalTasks;
        this.todoCount = todoCount;
        this.inProgressCount = inProgressCount;
        this.doneCount = doneCount;
        this.criticalCount = criticalCount;
        this.highCount = highCount;
        this.tagCounts = tagCounts;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getTodoCount() {
        return todoCount;
    }

    public void setTodoCount(long todoCount) {
        this.todoCount = todoCount;
    }

    public long getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(long inProgressCount) {
        this.inProgressCount = inProgressCount;
    }

    public long getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(long doneCount) {
        this.doneCount = doneCount;
    }

    public long getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(long criticalCount) {
        this.criticalCount = criticalCount;
    }

    public long getHighCount() {
        return highCount;
    }

    public void setHighCount(long highCount) {
        this.highCount = highCount;
    }

    public Map<String, Long> getTagCounts() {
        return tagCounts;
    }

    public void setTagCounts(Map<String, Long> tagCounts) {
        this.tagCounts = tagCounts;
    }
}
