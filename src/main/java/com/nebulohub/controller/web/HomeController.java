package com.nebulohub.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles the root URL.
 * **FIX:** Now serves the "index" (splash page) instead of redirecting.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        // Renderiza o template "index.html" como a página de splash
        return "index";
    }
}