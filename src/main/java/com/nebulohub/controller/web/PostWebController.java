package com.nebulohub.controller.web;


import com.nebulohub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Web Controller for serving Thymeleaf pages related to Posts.
 */
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostWebController {

    private final PostService postService;

    @GetMapping
    public String listPosts(
            Model model,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        // 1. Get the page of posts from the service
        Page<com.nebulohub.domain.post.dto.ReadPostDto> postPage = postService.findAll(pageable);
        
        // 2. Add the page data to the model
        model.addAttribute("postPage", postPage);
        model.addAttribute("pageTitle", "Startup Feed"); // For the <title> tag
        
        // 3. Return the name of the Thymeleaf template
        return "posts/list";
    }
}