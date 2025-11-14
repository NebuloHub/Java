package com.nebulohub.controller.web;

import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.ReadPostDto;
import com.nebulohub.domain.user.ReadUserDto;
import com.nebulohub.domain.user.UpdateUserDto;
import com.nebulohub.exception.DuplicateEntryException;
import com.nebulohub.service.CommentService;
import com.nebulohub.service.PostService;
import com.nebulohub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String viewUserProfile(
            @PathVariable Long id,
            Model model,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ReadUserDto user = userService.findById(id);
        Page<ReadPostDto> postPage = postService.findAllByUserId(id, pageable);
        Pageable commentsPageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<ReadCommentDto> commentPage = commentService.getCommentsFromUser(id, commentsPageable);


        model.addAttribute("profileUser", user);
        model.addAttribute("postPage", postPage);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("pageTitle", user.username() + "'s Profile");

        return "users/view";
    }


    @GetMapping("/{id}/edit")
    @PreAuthorize("#id == principal.id")
    public String showEditForm(@PathVariable Long id, Model model) {
        ReadUserDto user = userService.findById(id);
        UpdateUserDto updateDto = new UpdateUserDto(user.username(), user.email(), null, user.role());

        model.addAttribute("userDto", updateDto);
        model.addAttribute("userId", id);
        model.addAttribute("pageTitle", "Edit Your Profile");
        return "users/edit";
    }

    /**
     * Processa o formulário de edição de perfil.
     */
    @PostMapping("/{id}/edit")
    @PreAuthorize("#id == principal.id")
    public String handleEditForm(
            @PathVariable Long id,
            @Valid @ModelAttribute("userDto") UpdateUserDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("pageTitle", "Edit Your Profile");
            return "users/edit";
        }

        try {
            userService.update(id, dto);
            redirectAttributes.addFlashAttribute("profileSuccess", "Profile updated successfully!");
            return "redirect:/users/" + id;
        } catch (DuplicateEntryException e) {
            model.addAttribute("userId", id);
            model.addAttribute("pageTitle", "Edit Your Profile");
            model.addAttribute("globalError", e.getMessage());
            return "users/edit";
        }
    }

    /**
     * Processa a exclusão do usuário.
     */
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public String handleDeleteUser(
            @PathVariable Long id,
            Authentication authentication,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        ReadUserDto userToDelete = userService.findById(id);
        boolean isSelfDelete = authentication.getName().equals(userToDelete.email());

        userService.delete(id);

        if (isSelfDelete) {
            new SecurityContextLogoutHandler().logout(request, null, authentication);
            redirectAttributes.addFlashAttribute("successMessage", "Your account has been deleted.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "User " + userToDelete.username() + " has been deleted.");
            return "redirect:/";
        }
    }
}