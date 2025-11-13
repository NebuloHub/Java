package com.nebulohub.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles the root URL and redirects to the main posts feed.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        // Redirect the root URL "/" to the main post list "/posts"
        return "redirect:/posts";
    }
}