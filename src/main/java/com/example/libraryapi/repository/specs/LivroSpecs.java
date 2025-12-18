package com.example.libraryapi.repository.specs;

import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro generoLivro) {
        return (root, query, cb) -> cb.equal(root.get("genero"), generoLivro);
    }

    public static Specification<Livro> anoPublicacaoLivro(Integer anoPubli) {
        return ((root, query, cb) ->
                cb.equal(cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY"))
                        , anoPubli.toString()));
    }

    public static Specification<Livro> nomeAutorLike(String nomeAutor) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER);
            return cb.like( cb.upper(joinAutor.get("nome")), "%" + nomeAutor.toUpperCase() + "%" );
            //return cb.like(cb.upper(root.get("autor").get("nome")), "%" + nomeAutor.toUpperCase() + "%");
        };
    }
}
