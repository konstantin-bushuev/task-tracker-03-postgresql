package io.github.konstantinbushuev.tasktracker03postgresql.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequestDTO(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 30, message = "Title must not exceed 30 characters")
        String title,

        @Size(max = 100, message = "Description must not exceed 100 characters")
        String description
) {}
