package com.example.libraryapi.dto;

import com.example.libraryapi.model.GeneroLivro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Livro")
public record CadastroLivroDTO(
        @NotBlank
        @Size(max = 20)
        //@ISBN
        String isbn,
        @NotBlank
        @Size(max = 150)
        String titulo,
        @NotNull
        @Past(message = "Não pode ser uma data futura")
        @Schema(name = "data_de_publicacao")
        LocalDate dataPublicacao,
        @NotNull
        @Schema(name = "genero_livro")
        GeneroLivro generoLivro,
        BigDecimal preco,
        @NotNull
        UUID id_Autor
) {
}
