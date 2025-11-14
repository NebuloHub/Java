package com.nebulohub.controller.web;


import com.nebulohub.domain.comment.CreateCommentDto;
import com.nebulohub.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // <-- IMPORT ADDED
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentWebController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public String createComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute("newComment") CreateCommentDto dto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        // ... (no changes to this method) ...
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "Comment cannot be empty.");
            return "redirect:/posts/" + postId;
        }

        try {
            commentService.create(dto, authentication);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment posted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("commentError", "Error posting comment: " + e.getMessage());
        }

        return "redirect:/posts/" + postId + "#comments";
    }

    /**
     * **--- METHOD MODIFIED ---**
     * Handles the form submission for deleting a comment.
     * Now accepts redirect parameters to handle different redirect logic.
     */
    @PostMapping("/{postId}/delete/{commentId}")
    public String deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam(value = "redirect", required = false) String redirect, // <-- PARAM ADDED
            @RequestParam(value = "userId", required = false) Long userId,       // <-- PARAM ADDED
            RedirectAttributes redirectAttributes
    ) {
        try {
            commentService.delete(commentId);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("commentError", "Error deleting comment: " + e.getMessage());
        }

        // --- NEW REDIRECT LOGIC ---
        // If the redirect param is 'user' and we have a userId, redirect to the user's page
        if ("user".equals(redirect) && userId != null) {
            return "redirect:/users/" + userId;
        }

        // Otherwise, do the default redirect to the post page
        return "redirect:/posts/" + postId + "#comments";
    }
}