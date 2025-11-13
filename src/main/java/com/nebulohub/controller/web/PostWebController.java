package com.nebulohub.controller.web;


import com.nebulohub.domain.comment.CreateCommentDto;
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.CreatePostDto;
import com.nebulohub.domain.post.UpdatePostDto; // <-- IMPORT ADICIONADO
import com.nebulohub.domain.post.dto.ReadPostDto;
import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.service.CommentService;
import com.nebulohub.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize; // <-- IMPORT ADICIONADO
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "posts/list";
    }

    @GetMapping("/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("newPost", new CreatePostDto("", ""));
        return "posts/new";
    }

    @PostMapping
    public String createPost(
            @Valid @ModelAttribute("newPost") CreatePostDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "posts/new";
        }

        try {
            ReadPostDto newPost = postService.create(dto, authentication);
            redirectAttributes.addFlashAttribute("postSuccessKey", "post.new.success");
            return "redirect:/posts/" + newPost.id();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("postErrorKey", "post.new.error");
            redirectAttributes.addFlashAttribute("postErrorMessage", e.getMessage());
            return "redirect:/posts/new";
        }
    }

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
        model.addAttribute("pageTitle", post.title());

        model.addAttribute("newComment", new CreateCommentDto("", id));
        model.addAttribute("newRating", new SubmitRatingDto(null, id));

        // **NOVO:** Mensagem de sucesso ao editar
        if (model.containsAttribute("postSuccessKey")) {
            model.addAttribute("postSuccess", model.getAttribute("postSuccessKey"));
        }

        return "posts/view";
    }

    /**
     * **NOVO MÉTODO**
     * Exibe o formulário de edição de post.
     * Somente o autor pode ver.
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("@postRepository.findById(#id).get().getUser().getId() == principal.id")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        ReadPostDto post = postService.findById(id);
        // Preenche o DTO com os dados atuais
        UpdatePostDto updateDto = new UpdatePostDto(post.title(), post.description());

        model.addAttribute("postDto", updateDto);
        model.addAttribute("postId", id);
        return "posts/edit"; // Novo template
    }

    /**
     * **NOVO MÉTODO**
     * Processa o formulário de edição de post.
     * Somente o autor pode enviar.
     */
    @PostMapping("/{id}/edit")
    @PreAuthorize("@postRepository.findById(#id).get().getUser().getId() == principal.id")
    public String handleEditPostForm(
            @PathVariable Long id,
            @Valid @ModelAttribute("postDto") UpdatePostDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            return "posts/edit";
        }

        try {
            postService.update(id, dto);
            redirectAttributes.addFlashAttribute("postSuccessKey", "post.edit.success");
            return "redirect:/posts/" + id; // Volta para o post
        } catch (Exception e) {
            model.addAttribute("postId", id);
            model.addAttribute("postErrorKey", "post.edit.error");
            model.addAttribute("postErrorMessage", e.getMessage());
            return "posts/edit";
        }
    }

    /**
     * **NOVO MÉTODO**
     * Processa a exclusão do post.
     * Autor ou Admin podem fazer isso.
     */
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    public String handleDeletePost(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        postService.delete(id);
        // Adiciona uma mensagem de sucesso para a página de lista
        redirectAttributes.addFlashAttribute("postSuccess", "Post deleted successfully.");
        return "redirect:/posts"; // Volta para a lista de posts
    }
}