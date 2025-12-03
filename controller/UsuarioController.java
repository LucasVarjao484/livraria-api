package com.example.libraryapi.controller;

import com.example.libraryapi.controller.mappers.UsuarioMapper;
import com.example.libraryapi.dto.UsuarioDTO;
import com.example.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar", description = "Salva um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erros de validação.")
    })
    public void salvar(@RequestBody @Valid UsuarioDTO dto) {
        var usuario = usuarioMapper.toEntity(dto);
        usuarioService.salvar(usuario);
    }
}
