package com.example.libraryapi.repository;

import com.example.libraryapi.model.Usuario;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByLogin(String login);
    Usuario findByEmail(String email);
}
