package com.example.demo.service;

import com.example.demo.model.Priority;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStats;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(TaskStatus status, Priority priority, String search, String tag) {
        return taskRepository.findAll().stream()
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> priority == null || t.getPriority() == priority)
                .filter(t -> tag == null || tag.isBlank() || (t.getTags() != null && t.getTags().stream().anyMatch(tg -> tg.equalsIgnoreCase(tag.trim()))))
                .filter(t -> {
                    if (search == null || search.isBlank()) {
                        return true;
                    }
                    String q = search.toLowerCase().trim();
                    boolean matchTitle = t.getTitle() != null && t.getTitle().toLowerCase().contains(q);
                    boolean matchDesc = t.getDescription() != null && t.getDescription().toLowerCase().contains(q);
                    boolean matchAssignee = t.getAssignee() != null && t.getAssignee().toLowerCase().contains(q);
                    return matchTitle || matchDesc || matchAssignee;
                })
                .collect(Collectors.toList());
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        task.setId(null);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }
        if (task.getTags() == null) {
            task.setTags(new ArrayList<>());
        }
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updated) {
        return taskRepository.findById(id).map(existing -> {
            if (updated.getTitle() != null && !updated.getTitle().trim().isEmpty()) {
                existing.setTitle(updated.getTitle().trim());
            }
            if (updated.getDescription() != null) {
                existing.setDescription(updated.getDescription().trim());
            }
            if (updated.getPriority() != null) {
                existing.setPriority(updated.getPriority());
            }
            if (updated.getStatus() != null) {
                existing.setStatus(updated.getStatus());
            }
            if (updated.getAssignee() != null) {
                existing.setAssignee(updated.getAssignee().trim());
            }
            if (updated.getTags() != null) {
                existing.setTags(updated.getTags());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(existing);
        });
    }

    public Optional<Task> updateTaskStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id).map(existing -> {
            existing.setStatus(status);
            existing.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(existing);
        });
    }

    public boolean deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }

    public TaskStats getTaskStats() {
        List<Task> all = taskRepository.findAll();
        long total = all.size();
        long todo = all.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = all.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = all.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long critical = all.stream().filter(t -> t.getPriority() == Priority.CRITICAL).count();
        long high = all.stream().filter(t -> t.getPriority() == Priority.HIGH).count();

        Map<String, Long> tagCounts = new HashMap<>();
        for (Task t : all) {
            if (t.getTags() != null) {
                for (String tag : t.getTags()) {
                    tagCounts.put(tag, tagCounts.getOrDefault(tag, 0L) + 1);
                }
            }
        }

        return new TaskStats(total, todo, inProgress, done, critical, high, tagCounts);
    }
}
