package com.example.demo.repository;

import com.example.demo.model.Priority;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final Map<Long, Task> taskMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public TaskRepository() {
        seedInitialTasks();
    }

    private void seedInitialTasks() {
        save(new Task(
                null,
                "Setup Kubernetes Cluster Ingress & TLS",
                "Deploy NGINX ingress controller and configure Cert-Manager with Let's Encrypt for automatic HTTPS certificate issuance.",
                Priority.CRITICAL,
                TaskStatus.IN_PROGRESS,
                "DevOps Lead",
                Arrays.asList("k8s", "devops", "security")
        ));

        save(new Task(
                null,
                "Dockerize Spring Boot Microservices",
                "Create multi-stage Dockerfiles for Java 17 Spring Boot services to minimize image size and improve caching.",
                Priority.HIGH,
                TaskStatus.DONE,
                "Cloud Engineer",
                Arrays.asList("docker", "java", "cicd")
        ));

        save(new Task(
                null,
                "Configure Prometheus & Grafana Monitoring",
                "Set up cluster-wide metrics scraping with Prometheus Operator and build Grafana dashboards for latency and JVM memory.",
                Priority.HIGH,
                TaskStatus.TODO,
                "SRE Team",
                Arrays.asList("monitoring", "prometheus", "grafana")
        ));

        save(new Task(
                null,
                "Implement CI/CD GitHub Actions Workflow",
                "Automate Maven build, unit test execution, image vulnerability scan, and ArgoCD gitops sync.",
                Priority.MEDIUM,
                TaskStatus.DONE,
                "DevOps Engineer",
                Arrays.asList("cicd", "github-actions", "automation")
        ));

        save(new Task(
                null,
                "Database Indexing & Query Tuning",
                "Analyze slow query logs, add missing composite indexes, and optimize connection pooling settings.",
                Priority.LOW,
                TaskStatus.TODO,
                "Backend Developer",
                Arrays.asList("database", "performance", "sql")
        ));
    }

    public List<Task> findAll() {
        return taskMap.values().stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idSequence.getAndIncrement());
        }
        taskMap.put(task.getId(), task);
        return task;
    }

    public boolean deleteById(Long id) {
        return taskMap.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return taskMap.containsKey(id);
    }

    public long count() {
        return taskMap.size();
    }
}
