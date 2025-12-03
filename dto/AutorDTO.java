package com.example.libraryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Autor")
public record AutorDTO(
        UUID id,
        @NotBlank
        @Size(min = 2, max = 100)
        String nome,
        @NotNull
        @Past
        @Schema(name = "data_de_nascimento")
        LocalDate dataNascimento,
        @NotBlank(message = "teste")
        @Size(max = 50)
        String nacionalidade) {
}
