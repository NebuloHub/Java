// File: src/main/java/com/nebulohub/controller/web/RatingWebController.java (NEW FILE)
package com.nebulohub.controller.web;

import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingWebController {

    private final RatingService ratingService;

    /**
     * Handles the form submission for rating a post.
     * This will be called from the "posts/view" page.
     */
    @PostMapping("/post/{postId}")
    public String submitRating(
            @PathVariable Long postId,
            @Valid @ModelAttribute("newRating") SubmitRatingDto dto,
            Authentication authentication, // Get the logged-in user
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Call the upsert service method
            ratingService.createOrUpdate(dto, authentication);
            redirectAttributes.addFlashAttribute("ratingSuccess", "Your rating of " + dto.ratingValue() + " was saved!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ratingError", "Error saving rating: " + e.getMessage());
        }

        // Redirect back to the post page
        return "redirect:/posts/" + postId;
    }
}