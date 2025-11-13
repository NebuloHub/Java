// File: src/main/java/com/nebulohub/controller/web/PostWebController.java (UPDATED)
package com.nebulohub.controller.web;

import com.nebulohub.domain.comment.CreateCommentDto;
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.dto.ReadPostDto;
import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.service.CommentService;
import com.nebulohub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReadPostDto> postPage = postService.findAll(pageable);
        model.addAttribute("postPage", postPage);
        model.addAttribute("pageTitle", "Startup Feed");
        return "posts/list";
    }

    /**
     * **UPDATED MAPPING**
     * Handles the "view" page for a single post.
     * The regex `:[0-9]+` ensures this only matches IDs that are numbers.
     */
    @GetMapping("/{id:[0-9]+}") // <-- THIS IS THE FIX
    public String viewPost(
            @PathVariable Long id,
            Model model,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable commentPageable
    ) {
        // 1. Get the main post data
        ReadPostDto post = postService.findById(id);

        // 2. Get all comments for this post (paginated)
        Page<ReadCommentDto> commentPage = commentService.getCommentsForPost(id, commentPageable);

        // 3. Add all data to the model
        model.addAttribute("post", post);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("pageTitle", post.title());

        // 4. Add empty "form-backing objects" for the new forms
        model.addAttribute("newComment", new CreateCommentDto("", null)); // For the comment form
        // We pre-fill the postId in the rating DTO
        model.addAttribute("newRating", new SubmitRatingDto(null, id));

        return "posts/view";
    }
}