package com.example.libraryapi.service;

import com.example.libraryapi.dto.AutorDTO;
import com.example.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.repository.AutorRepository;
import com.example.libraryapi.repository.LivroRepository;
import com.example.libraryapi.security.SecurityService;
import com.example.libraryapi.validator.AutorValidator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {
    private final AutorRepository autorRepository;
    private final AutorValidator autorValidator;
    private final LivroRepository livroRepository;
    private final SecurityService securityService;

    public AutorService(AutorRepository autorRepository, AutorValidator autorValidator, LivroRepository livroRepository, SecurityService securityService) {
        this.autorRepository = autorRepository;
        this.autorValidator = autorValidator;
        this.livroRepository = livroRepository;
        this.securityService = securityService;
    }

    public Autor salvar(Autor autor) {
        autorValidator.validar(autor);
        Usuario usuario = securityService.obterUsuarioLogado();
        autor.setUsuario(usuario);
        return autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id){
        return autorRepository.findById(id);
    }

    public void deletar(Autor autor) {
        if(possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Não é permitido excluir autores que possuem livros");
        }
        autorRepository.delete(autor);
    }

    public List<Autor> buscarAutores(String nome, String nacionalidade) {
        /*if(nome != null && nacionalidade != null){
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if(nome != null){
            return autorRepository.findByNome(nome);
        }

        if(nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade);
        }

        return autorRepository.findAll();*/
        return autorRepository.encontrarParam(nome, nacionalidade);
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento", "dataCadastro")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, matcher);
        return autorRepository.findAll(autorExample);
    }

    public void atualizar(Autor autor) {
        if(autor.getId() == null){
            throw new IllegalArgumentException("Para atualizar, é necessário que o autor já esteja salvo na base.");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}
