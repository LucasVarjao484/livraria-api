package com.example.libraryapi.controller.mappers;

import com.example.libraryapi.dto.AutorDTO;
import com.example.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    //@Mapping(source = "", target = "")
    Autor toEntity(AutorDTO autorDTO);

    AutorDTO toDTO(Autor autor);
}
