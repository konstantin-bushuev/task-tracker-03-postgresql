package io.github.konstantinbushuev.tasktracker03postgresql.repository;

import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    private Long nextId = 1L;

    public Long nextId() {
        return nextId++;
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }

    public Task save(Task task) {
        tasks.add(task);
        return task;
    }

    public boolean deleteById(Long id) {
        return  tasks.removeIf(task -> task.id().equals(id));
    }
}
