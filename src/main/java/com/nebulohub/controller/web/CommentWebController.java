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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentWebController {

    private final CommentService commentService;

    /**
     * Handles the form submission for adding a new comment.
     * This will be called from the "posts/view" page.
     */
    @PostMapping("/post/{postId}")
    public String createComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute("newComment") CreateCommentDto dto, // DTO from the form
            BindingResult bindingResult,
            Authentication authentication, // Get the logged-in user
            RedirectAttributes redirectAttributes
    ) {
        // If the form has errors (e.g., empty comment), redirect with an error
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "Comment cannot be empty.");
            return "redirect:/posts/" + postId;
        }

        try {
            // Call the service, passing the DTO and the logged-in user
            commentService.create(dto, authentication);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment posted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("commentError", "Error posting comment: " + e.getMessage());
        }

        // Redirect back to the post page, jumping to the comments section
        return "redirect:/posts/" + postId + "#comments";
    }

    /**
     * **--- NEW METHOD ---**
     * Handles the form submission for deleting a comment.
     * The security is handled by @PreAuthorize on the service layer.
     */
    @PostMapping("/{postId}/delete/{commentId}")
    public String deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Security is checked inside the service method
            commentService.delete(commentId);
            redirectAttributes.addFlashAttribute("commentSuccess", "Comment deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("commentError", "Error deleting comment: " + e.getMessage());
        }

        // Redirect back to the post page, jumping to the comments section
        return "redirect:/posts/" + postId + "#comments";
    }
}