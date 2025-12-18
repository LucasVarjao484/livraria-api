package com.example.libraryapi.controller.mappers;

import com.example.libraryapi.dto.CadastroLivroDTO;
import com.example.libraryapi.dto.ResultadoPesquisaLivroDTO;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java(autorRepository.findById(cadastroLivroDTO.id_Autor()).orElse(null) )")
    @Mapping(source = "generoLivro", target = "genero")
    public abstract Livro toEntity(CadastroLivroDTO cadastroLivroDTO);

    @Mapping(source = "autor", target = "autorDTO")
    @Mapping(source = "genero", target = "generoLivro")
    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
