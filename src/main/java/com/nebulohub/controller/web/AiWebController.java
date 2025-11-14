package com.nebulohub.controller.web;

import com.nebulohub.domain.post.ReadPostDto;
import com.nebulohub.service.AiService;
import com.nebulohub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale; // <-- IMPORT ADICIONADO

@Controller
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiWebController {

    private final AiService aiService;
    private final PostService postService;


    @PostMapping("/critique/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    public String getAiCritique(
            @PathVariable Long postId,
            RedirectAttributes redirectAttributes,
            Locale locale
    ) {
        try {
            // 1. Busca o conteúdo do post
            ReadPostDto post = postService.findById(postId);

            // 2. Chama o serviço de IA, passando o idioma
            String critique = aiService.getStartupCritique(
                    post.description(),
                    locale.getLanguage() // <-- Passa "en", "pt", etc.
            );

            // 3. Adiciona o resultado como Flash Attribute
            redirectAttributes.addFlashAttribute("aiCritique", critique);

        } catch (Exception e) {
            // 4. Em caso de erro, informa o usuário
            redirectAttributes.addFlashAttribute("aiError", "Erro ao contatar a IA: " + e.getMessage());
        }

        // 5. Redireciona de volta
        return "redirect:/posts/" + postId + "#ai-analysis";
    }
}