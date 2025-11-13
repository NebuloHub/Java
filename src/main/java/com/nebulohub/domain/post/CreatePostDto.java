package com.nebulohub.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern; // <-- IMPORT ADICIONADO
import jakarta.validation.constraints.Size;


/**
 * O userId será recuperado automaticamente
 * do token do usuário autenticado.
 */
public record CreatePostDto(
        @NotBlank(message = "{register.validation.username.required}") // Reutiliza a chave
        @Size(max = 255, message = "Title is too long")
        String title,

        @NotBlank(message = "{register.validation.password.required}") // Reutiliza a chave
        String description,

        /**
         * **NOVO CAMPO**
         * Valida se está vazio OU se é uma URL válida de imagem.
         */
        @Pattern(regexp = "^($|http(s?):\\/\\/.*\\.(?:jpg|jpeg|gif|png|webp))$",
                message = "{post.validation.imageUrl}")
        String imageUrl
)

{

}