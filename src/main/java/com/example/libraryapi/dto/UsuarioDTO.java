package com.example.libraryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public record UsuarioDTO(
        @NotBlank(message = "campo obrigatório")
        String login,
        @Email(message = "inválido")
        @NotBlank(message = "campo obrigatório")
        String email,
        @NotBlank(message = "campo obrigatório")
        String senha,
        List<String> roles) {
}
