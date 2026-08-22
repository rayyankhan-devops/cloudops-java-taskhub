package com.example.demo;

import com.example.demo.model.Priority;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].title", notNullValue()));
    }

    @Test
    void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", notNullValue()));
    }

    @Test
    void testCreateTask() throws Exception {
        Task newTask = new Task(
                null,
                "Automate Security Scans with Trivy",
                "Integrate container vulnerability scanning in pipeline.",
                Priority.HIGH,
                TaskStatus.TODO,
                "Security Eng",
                List.of("security", "trivy")
        );

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Automate Security Scans with Trivy")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        mockMvc.perform(patch("/api/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("DONE")));
    }

    @Test
    void testDeleteTask() throws Exception {
        // First create a task to delete
        Task task = new Task(null, "Task To Delete", "Test", Priority.LOW, TaskStatus.TODO, "Dev", List.of());
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Task created = objectMapper.readValue(response, Task.class);

        mockMvc.perform(delete("/api/tasks/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.application", notNullValue()));
    }

    @Test
    void testSystemMetricsEndpoint() throws Exception {
        mockMvc.perform(get("/api/system/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("HEALTHY")))
                .andExpect(jsonPath("$.javaVersion", notNullValue()))
                .andExpect(jsonPath("$.totalMemoryMb", greaterThan(0)));
    }
}
