package io.github.konstantinbushuev.tasktracker03postgresql.repository;

import io.github.konstantinbushuev.tasktracker03postgresql.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
