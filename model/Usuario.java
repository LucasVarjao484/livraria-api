package com.example.libraryapi.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column
    public String login;

    @Column
    public String senha;

    @Column
    public String email;

    @Type(ListArrayType.class)
    @Column(name = "roles", columnDefinition = "varchar[]")
    public List<String> roles;
}
