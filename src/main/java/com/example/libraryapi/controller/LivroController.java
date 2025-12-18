package com.example.libraryapi.controller;

import com.example.libraryapi.controller.mappers.LivroMapper;
import com.example.libraryapi.dto.CadastroLivroDTO;
import com.example.libraryapi.dto.ErroResposta;
import com.example.libraryapi.dto.ResultadoPesquisaLivroDTO;
import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado.")
    })
    public ResponseEntity<?> salvar(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {
        Livro livro = livroMapper.toEntity(cadastroLivroDTO);
        livroService.salvarLivro(livro);
        var url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Retorna dados de um livro pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
    })
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id){
        return livroService.obterDetalhes(UUID.fromString(id))
                .map( livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um livro pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
    public ResponseEntity<Object> deletarPorId(@PathVariable String id) {
        return livroService.obterDetalhes(UUID.fromString(id)).map(livro -> {
            livroService.deletar(livro);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Busca um livro pelo seus atributos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisaId(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nome-autor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro generoLivro,
            @RequestParam(value = "ano-publicacao", required = false) Integer anoPubli,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "numero-itens", defaultValue = "10") Integer numeroItem
            ){
        Page<Livro> resultado = livroService.buscar(isbn, titulo, nomeAutor, generoLivro, anoPubli, pagina, numeroItem);
        Page<ResultadoPesquisaLivroDTO> lista = resultado.map(livroMapper::toDTO);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza um livro pelo seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
    public ResponseEntity<Object> editarLivro(
            @PathVariable UUID id,
            @RequestBody CadastroLivroDTO dto) {
        return livroService.obterDetalhes(id)
                .map(livro -> {
                    Livro livro1 = livroMapper.toEntity(dto);

                    livro.setTitulo(livro1.getTitulo());
                    livro.setIsbn(livro1.getIsbn());
                    livro.setDataPublicacao(livro1.getDataPublicacao());
                    livro.setPreco(livro1.getPreco());
                    livro.setGenero(livro1.getGenero());
                    livro.setAutor(livro1.getAutor());

                    livroService.editar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());


    }
}
