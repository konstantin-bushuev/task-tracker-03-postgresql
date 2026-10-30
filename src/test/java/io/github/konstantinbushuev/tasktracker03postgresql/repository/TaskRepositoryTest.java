package io.github.konstantinbushuev.tasktracker03postgresql.repository;

import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import io.github.konstantinbushuev.tasktracker03postgresql.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void save_shouldPersistTask() {
        LocalDateTime now = LocalDateTime.now();

        Task task = new Task(
                null,
                "Task title",
                "Task description",
                TaskStatus.TODO,
                now,
                now
        );

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Task title", savedTask.getTitle());
        assertEquals("Task description", savedTask.getDescription());
        assertEquals(TaskStatus.TODO, savedTask.getStatus());
    }

    @Test
    void findById_shouldReturnSavedTask() {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(
                null,
                "Task title",
                "Task description",
                TaskStatus.TODO,
                now,
                now
        );

        Task savedTask = taskRepository.save(task);

        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        assertTrue(foundTask.isPresent());
        assertEquals("Task title", foundTask.get().getTitle());
        assertEquals("Task description", foundTask.get().getDescription());
    }

}
