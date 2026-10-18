package io.github.konstantinbushuev.tasktracker03postgresql.service;

import io.github.konstantinbushuev.tasktracker03postgresql.dto.TaskRequestDTO;
import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import io.github.konstantinbushuev.tasktracker03postgresql.model.TaskStatus;
import io.github.konstantinbushuev.tasktracker03postgresql.repository.TaskRepository;
import io.github.konstantinbushuev.tasktracker03postgresql.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Task updateTask(Long id, TaskRequestDTO request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.updateDetails(request.title(), request.description());
        task.setUpdatedAt(LocalDateTime.now());

        return task;
    }

    @Transactional
    public Task completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.complete();
        task.setUpdatedAt(LocalDateTime.now());

        return task;
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }

}
