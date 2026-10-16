package io.github.konstantinbushuev.tasktracker03postgresql.service;

import io.github.konstantinbushuev.tasktracker03postgresql.dto.TaskRequestDTO;
import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import io.github.konstantinbushuev.tasktracker03postgresql.model.TaskStatus;
import io.github.konstantinbushuev.tasktracker03postgresql.repository.TaskRepository;
import io.github.konstantinbushuev.tasktracker03postgresql.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskRequestDTO request) {
        LocalDateTime currentTime = LocalDateTime.now();

        Task task = new Task(
                null,
                request.title(),
                request.description(),
                TaskStatus.TODO,
                currentTime,
                currentTime
        );

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateTask(Long id, TaskRequestDTO request) {
        Task initialTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        Task task = new Task(
                id,
                request.title(),
                request.description(),
                initialTask.getStatus(),
                initialTask.getCreatedAt(),
                LocalDateTime.now()
        );

        return taskRepository.save(task);
    }

    public Task completeTask(Long id) {
        Task initialTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        Task task = new Task(
                id,
                initialTask.getTitle(),
                initialTask.getDescription(),
                TaskStatus.DONE,
                initialTask.getCreatedAt(),
                LocalDateTime.now()
        );

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }

}
