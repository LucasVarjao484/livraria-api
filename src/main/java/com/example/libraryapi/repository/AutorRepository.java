package com.example.libraryapi.repository;

import com.example.libraryapi.model.Autor;
import jakarta.validation.constraints.Past;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
    List<Autor> findByNome(String nome);
    List<Autor> findByNacionalidade(String nacionalidade);
    List<Autor> findByNomeAndNacionalidade(String nome, String nacionalidade);

    Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(
            String nome, LocalDate dataNascimento, String nacionalidade
    );

/*    boolean existsByNomeAndDataNascimentoAndNacionalidade(
            String nome, LocalDate dataNascimento, String nacionalidade
    );*/

    @Query("select a from Autor a where(lower(:nome) is null or lower(a.nome) like lower(concat('%', :nome, '%'))) and (lower(:nacionalidade) is null or lower(a.nacionalidade) like lower(concat('%', :nacionalidade, '%')))")
    List<Autor> encontrarParam(@Param(value = "nome") String nome, @Param(value = "nacionalidade") String nacionalidade);
}
