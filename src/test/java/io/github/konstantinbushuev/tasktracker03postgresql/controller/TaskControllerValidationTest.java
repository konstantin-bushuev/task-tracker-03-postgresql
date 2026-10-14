package io.github.konstantinbushuev.tasktracker03postgresql.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTask_shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        String requestJson = """
                {
                    "title": "   ",
                    "description": "Some description"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createTask_shouldReturnBadRequestWhenTitleIsTooLong() throws Exception {
        String requestJson = """
            {
                "title": "1234567890123456789012345678901",
                "description": "Some description"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createTask_shouldReturnBadRequestWhenJsonIsInvalid() throws Exception {
        String invalidJson = """
            {
                "title": "Task title",
                "description":
            }
            """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }


}