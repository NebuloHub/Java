package com.nebulohub.domain.post;

import jakarta.validation.constraints.Pattern; // <-- IMPORT ADICIONADO
import jakarta.validation.constraints.Size;

/**
 * DTO para atualização parcial de um post.
 */
public record UpdatePostDto(
        @Size(max = 255, message = "Title is too long")
        String title,

        String description,

        /**
         * **NOVO CAMPO**
         * Valida se está vazio OU se é uma URL válida de imagem.
         */
        @Pattern(regexp = "^($|http(s?):\\/\\/.*\\.(?:jpg|jpeg|gif|png|webp))$",
                message = "{post.validation.imageUrl}")
        String imageUrl
) {
}