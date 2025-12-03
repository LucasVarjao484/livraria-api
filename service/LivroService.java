package com.example.libraryapi.service;

import com.example.libraryapi.dto.CadastroLivroDTO;
import com.example.libraryapi.dto.ResultadoPesquisaLivroDTO;
import com.example.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.repository.LivroRepository;
import com.example.libraryapi.repository.specs.LivroSpecs;
import com.example.libraryapi.security.SecurityService;
import com.example.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.HTMLDocument;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;
    private final SecurityService securityService;

    public Livro salvarLivro(Livro livro) {
        livroValidator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterDetalhes(UUID uuid) {
        return livroRepository.findById(uuid);
    }

    public void deletar(Livro livro) {
        livroValidator.validar(livro);
        livroRepository.delete(livro);
    }

    public Page<Livro> buscar(String isbn,
                              String titulo,
                              String nomeAutor,
                              GeneroLivro generoLivro,
                              Integer anoPubli,
                              Integer pagina,
                              Integer numeroItem){
        Specification<Livro> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if(isbn != null) {
            spec = spec.and(LivroSpecs.isbnEqual(isbn));
        }
        if(titulo != null) {
            spec = spec.and(LivroSpecs.tituloLike(titulo));
        }
        if(nomeAutor != null) {
            spec = spec.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }
        if(generoLivro != null) {
            spec = spec.and(LivroSpecs.generoEqual(generoLivro));
        }
        if(anoPubli != null) {
            spec = spec.and(LivroSpecs.anoPublicacaoLivro(anoPubli));
        }

        Pageable pageRequest = PageRequest.of(pagina, numeroItem);

        return livroRepository.findAll(spec, pageRequest);
    }

    @Transactional
    public void editar(Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessário que o livro já esteja salvo na base.");
        }
        livroValidator.validar(livro);
        //livroRepository.save(livro);
    }
}
