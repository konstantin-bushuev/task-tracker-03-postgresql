package io.github.konstantinbushuev.tasktracker03postgresql.service;

import io.github.konstantinbushuev.tasktracker03postgresql.dto.TaskRequestDTO;
import io.github.konstantinbushuev.tasktracker03postgresql.exception.TaskNotFoundException;
import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import io.github.konstantinbushuev.tasktracker03postgresql.model.TaskStatus;
import io.github.konstantinbushuev.tasktracker03postgresql.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldSaveTaskWithTodoStatus() {
        TaskRequestDTO request = new TaskRequestDTO(
                "Task title",
                "This is task description"
        );

        when(taskRepository.nextId()).thenReturn(1L);
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.createTask(request);

        assertEquals(1L, result.id());
        assertEquals("Task title", result.title());
        assertEquals("This is task description", result.description());
        assertEquals(TaskStatus.TODO, result.status());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());
        assertEquals(result.createdAt(), result.updatedAt());

        verify(taskRepository).save(result);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task task1 = new Task(
                1L, "Task 1", "Description 1",
                TaskStatus.TODO,
                LocalDateTime.now(), LocalDateTime.now()
        );

        Task task2 = new Task(
                2L, "Task 2", "Description 2",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));

        verify(taskRepository).findAll();
    }

    @Test
    void getTask_shouldReturnTaskWhenExists() {
        Task task = new Task(
                1L, "Task title", "This is task description",
                TaskStatus.TODO,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L);

        assertEquals(task, result);
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById(111L)).thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(111L)
        );

        verify(taskRepository).findById(111L);
    }

    @Test
    void updateTask_shouldUpdateTaskFields() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        Task existingTask = new Task(
                1L, "Old title", "Old description",
                TaskStatus.IN_PROGRESS, createdAt, createdAt
        );

        TaskRequestDTO request = new TaskRequestDTO(
                "New title", "New description"
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(1L, request);

        assertEquals("New title", result.title());
        assertEquals("New description", result.description());
        assertEquals(TaskStatus.IN_PROGRESS, result.status());
        assertEquals(createdAt, result.createdAt());
        assertFalse(result.updatedAt().isBefore(createdAt));

        verify(taskRepository).save(result);
    }

    @Test
    void updateTask_shouldThrowExceptionWhenNotFound() {
        TaskRequestDTO request = new TaskRequestDTO(
                "New title", "New description"
        );

        when(taskRepository.findById(111L)).thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(111L, request)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void completeTask_shouldSetStatusToDoneAndPreserveOtherFields() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);

        Task existingTask = new Task(
                1L,
                "Task title",
                "This is task description",
                TaskStatus.IN_PROGRESS,
                createdAt,
                createdAt
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.completeTask(1L);

        assertEquals(TaskStatus.DONE, result.status());
        assertEquals(existingTask.title(), result.title());
        assertEquals(existingTask.description(), result.description());
        assertEquals(existingTask.createdAt(), result.createdAt());
        assertFalse(result.updatedAt().isBefore(createdAt));

        verify(taskRepository).save(result);
    }

    @Test
    void completeTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById(111L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.completeTask(111L)
        );

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldDeleteTaskWhenExists() {
        when(taskRepository.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.deleteById(111L)).thenReturn(false);

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(111L)
        );

        verify(taskRepository).deleteById(111L);
    }


}