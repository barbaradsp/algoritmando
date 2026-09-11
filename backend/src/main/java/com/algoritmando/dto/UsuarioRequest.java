package com.algoritmando.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(
                min = 2,
                max = 100,
                message = "O nome deve ter entre 2 e 100 caracteres"
        )
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        String email

) {
}