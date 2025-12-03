package com.example.libraryapi.validator;

import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void validar(Autor autor) {
        if(existeCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }

    }

    private boolean existeCadastrado(Autor autor) {
        Optional<Autor> autorEncontrado = autorRepository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade());

        if (autor.getId() == null) {
            // se é um novo autor (sem ID ainda)
            return autorEncontrado.isPresent();
        }

        // se é uma atualização, só retorna true se encontrou outro autor diferente
        return autorEncontrado.isPresent() && !autor.getId().equals(autorEncontrado.get().getId());
    }
}
