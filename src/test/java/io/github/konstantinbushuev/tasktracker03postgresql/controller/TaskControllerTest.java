package io.github.konstantinbushuev.tasktracker03postgresql.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllTasks_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        String requestJson = """
            {
                "title": "Task title",
                "description": "Task description"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Task title"))
                .andExpect(jsonPath("$.description").value("Task description"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void getTask_shouldReturnTaskWhenExists() throws Exception {
        String requestJson = """
            {
                "title": "Task title",
                "description": "Task description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int taskId = com.jayway.jsonpath.JsonPath.read(response, "$.id");

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Task title"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        String createJson = """
            {
                "title": "Old title",
                "description": "Old description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int taskId = com.jayway.jsonpath.JsonPath.read(response, "$.id");

        String updateJson = """
            {
                "title": "New title",
                "description": "New description"
            }
            """;

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("New title"))
                .andExpect(jsonPath("$.description").value("New description"));
    }

    @Test
    void completeTask_shouldReturnTaskWithDoneStatus() throws Exception {
        String createJson = """
            {
                "title": "Task title",
                "description": "Task description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int taskId = com.jayway.jsonpath.JsonPath.read(response, "$.id");

        mockMvc.perform(patch("/api/tasks/{id}/complete", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void deleteTask_shouldReturnNoContentAndRemoveTask() throws Exception {
        String createJson = """
            {
                "title": "Task title",
                "description": "Task description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int taskId = com.jayway.jsonpath.JsonPath.read(response, "$.id");

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }



}