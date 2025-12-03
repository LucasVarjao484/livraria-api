package com.example.libraryapi.controller;

import com.example.libraryapi.controller.mappers.AutorMapper;
import com.example.libraryapi.dto.AutorDTO;
import com.example.libraryapi.dto.ErroResposta;
import com.example.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.service.AutorService;
import com.example.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
@Tag(name = "Autores")
@Slf4j
public class AutorController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;
    private final UsuarioService usuarioService;

    public AutorController(AutorService autorService, AutorMapper autorMapper, UsuarioService usuarioService) {
        this.autorService = autorService;
        this.autorMapper = autorMapper;
        this.usuarioService = usuarioService;
    }
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO/*, Authentication authentication*/) {
        log.info("Cadastrando autor: {}", autorDTO.nome());
        Autor autor1 = autorMapper.toEntity(autorDTO);
        //UserDetails user = (UserDetails) authentication.getPrincipal();
        //autor1.setIdUsuario(usuarioService.obterPorLogin(user.getUsername()).getId());
        autorService.salvar(autor1);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autor1.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Retorna dados de um autor pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
    })
    public ResponseEntity<AutorDTO> obterAutor(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        return autorService.obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO autorDTO = autorMapper.toDTO(autor);
                    return ResponseEntity.ok(autorDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um autor pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "400", description = "Autor possui livros.")
    })
    public ResponseEntity<Object> deletarAutor(@PathVariable String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        log.info("Deletando autor de ID: {}", id);
        autorService.deletar(autorOptional.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'OPERADOR')")
    @Operation(summary = "Pesquisar", description = "Busca um autor pelo seus atributos")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<AutorDTO>> pesquisaAutor(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){


        List<Autor> resultado = autorService.buscarAutores(nome, nacionalidade);
        List<AutorDTO> lista = resultado.stream()
                .map(autorMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza um autor pelo seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Autor atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Object> atualizarAutor(@PathVariable String id, @RequestBody @Valid AutorDTO autorDTO) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        log.info("Atualizando autor de ID: {}", id);
        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());

        autorService.atualizar(autor);

        return ResponseEntity.noContent().build();

    }
}
