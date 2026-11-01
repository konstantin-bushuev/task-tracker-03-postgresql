package io.github.konstantinbushuev.tasktracker03postgresql.service;

import io.github.konstantinbushuev.tasktracker03postgresql.dto.TaskRequestDTO;
import io.github.konstantinbushuev.tasktracker03postgresql.exception.TaskNotFoundException;
import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import io.github.konstantinbushuev.tasktracker03postgresql.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Test
    void createTask_shouldPersistTaskInDatabase() {
        TaskRequestDTO request = new TaskRequestDTO(
                "Integration test task",
                "Task description"
        );

        Task createdTask = taskService.createTask(request);

        assertNotNull(createdTask.getId());
        assertEquals("Integration test task", createdTask.getTitle());
        assertEquals("Task description", createdTask.getDescription());
        assertEquals(TaskStatus.TODO, createdTask.getStatus());

        Task foundTask = taskService.getTask(createdTask.getId());

        assertEquals(createdTask.getId(), foundTask.getId());
        assertEquals("Integration test task", foundTask.getTitle());
    }

    @Test
    void updateTask_shouldPersistChangesInDatabase() {
        TaskRequestDTO createRequest = new TaskRequestDTO(
                "Original title",
                "Original description"
        );

        Task createdTask = taskService.createTask(createRequest);

        TaskRequestDTO updateRequest = new TaskRequestDTO(
                "Updated title",
                "Updated description"
        );

        taskService.updateTask(createdTask.getId(), updateRequest);

        Task foundTask = taskService.getTask(createdTask.getId());

        assertEquals("Updated title", foundTask.getTitle());
        assertEquals("Updated description", foundTask.getDescription());
        assertEquals(createdTask.getCreatedAt(), foundTask.getCreatedAt());
        assertNotNull(foundTask.getUpdatedAt());
    }

    @Test
    void deleteTask_shouldRemoveTaskFromDatabase() {
        TaskRequestDTO request = new TaskRequestDTO(
                "Task to delete",
                "Temporary description"
        );

        Task createdTask = taskService.createTask(request);

        taskService.deleteTask(createdTask.getId());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(createdTask.getId())
        );
    }
}