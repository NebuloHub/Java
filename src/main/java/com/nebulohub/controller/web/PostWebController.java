package com.nebulohub.controller.web;


import com.nebulohub.domain.comment.CreateCommentDto;
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.CreatePostDto;
import com.nebulohub.domain.post.dto.ReadPostDto;
import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.service.CommentService;
import com.nebulohub.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web Controller for serving Thymeleaf pages related to Posts.
 */
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostWebController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public String listPosts(
            Model model,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<ReadPostDto> postPage = postService.findAll(pageable);
        model.addAttribute("postPage", postPage);
        // pageTitle é pego do #{post.list.title} no template
        return "posts/list";
    }

    /**
     * Exibe o formulário para criar um novo post.
     */
    @GetMapping("/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("newPost", new CreatePostDto("", ""));
        // pageTitle é pego do #{post.new.pageTitle} no template
        return "posts/new";
    }

    /**
     * Lida com a submissão do formulário de novo post.
     */
    @PostMapping
    public String createPost(
            @Valid @ModelAttribute("newPost") CreatePostDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            // Se falhar, recarrega a página. pageTitle será pego do template.
            return "posts/new";
        }

        try {
            ReadPostDto newPost = postService.create(dto, authentication);
            // **FIX i18n**: Usando uma chave para a mensagem de sucesso
            redirectAttributes.addFlashAttribute("postSuccessKey", "post.new.success");
            return "redirect:/posts/" + newPost.id();
        } catch (Exception e) {
            // **FIX i18n**: Usando uma chave para a mensagem de erro
            // Nota: GlobalExceptionHandler pode pegar isso, mas é uma boa proteção.
            redirectAttributes.addFlashAttribute("postErrorKey", "post.new.error");
            redirectAttributes.addFlashAttribute("postErrorMessage", e.getMessage());
            return "redirect:/posts/new";
        }
    }

    /**
     * Exibe a página de detalhes para um único post, incluindo seus comentários.
     */
    @GetMapping("/{id}")
    public String viewPost(
            @PathVariable Long id,
            Model model,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        ReadPostDto post = postService.findById(id);
        Page<ReadCommentDto> commentPage = commentService.getCommentsForPost(id, pageable);

        model.addAttribute("post", post);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("pageTitle", post.title()); // O título do post é dinâmico, está correto

        model.addAttribute("newComment", new CreateCommentDto("", id));
        model.addAttribute("newRating", new SubmitRatingDto(null, id));

        return "posts/view";
    }
}