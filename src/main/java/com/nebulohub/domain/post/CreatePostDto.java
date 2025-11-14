package com.nebulohub.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern; // <-- IMPORT ADICIONADO
import jakarta.validation.constraints.Size;



public record CreatePostDto(
        @NotBlank(message = "{register.validation.username.required}")
        @Size(max = 255, message = "Title is too long")
        String title,

        @NotBlank(message = "{register.validation.password.required}")
        String description,

        @Pattern(regexp = "^($|http(s?):\\/\\/.*\\.(?:jpg|jpeg|gif|png|webp))$",
                message = "{post.validation.imageUrl}")
        String imageUrl
)

{

}