package com.example.libraryapi.repository;

import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {
    boolean existsByAutor(Autor autor);

    @Query("select l from Livro l join l.autor a where(lower(:isbn) is null or lower(l.isbn) = lower(:isbn)) " +
            "and (lower(:titulo) is null or lower(l.titulo) = lower(:titulo)) " +
            "and (lower(:nomeAutor) is null or lower(a.nome) = lower(:nomeAutor)) " +
            "and (:generoLivro is null or l.genero = :generoLivro) " +
            "and (:anoPubli is null or l.dataPublicacao = :anoPubli)")
    List<Livro> testes(@Param(value = "isbn") String isbn,
                       @Param(value = "titulo") String titulo,
                       @Param(value = "nomeAutor") String nomeAutor,
                       @Param(value = "generoLivro") GeneroLivro generoLivro,
                       @Param(value = "anoPubli") Integer anoPubli);


    Optional<Livro> findByIsbn(String isbn);
}


//"select l from Livro l join l.autor a where(lower(:isbn) is null or lower(l.isbn) = lower(:isbn)) " +
//            "and (lower(:titulo) is null or lower(l.titulo) = lower(:titulo)) " +
//            "and (lower(:nomeAutor) is null or lower(a.nome) = lower(:nomeAutor)) " +
//            "and (:generoLivro is null or l.genero = :generoLivro) " +
//            "and (:anoPubli is null or function('YEAR', l.dataPublicacao) = :anoPubli)"