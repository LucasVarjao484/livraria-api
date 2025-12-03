package com.example.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {
    default URI gerarHeaderLocation(UUID uuid) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
    }
}
