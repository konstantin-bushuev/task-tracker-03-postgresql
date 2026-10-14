package io.github.konstantinbushuev.tasktracker03postgresql.model;

import java.time.LocalDateTime;

public record Task(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}