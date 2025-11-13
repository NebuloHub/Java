package com.nebulohub.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web Controller for serving the Login page.
 * Based on br.com.fiap.otmav.controller.web.WebAuthController
 */
@Controller
public class WebAuthController {

    @GetMapping("/login")
    public String loginForm(
            Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "forbidden", required = false) String forbidden
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out.");
        }
        if (forbidden != null) {
            model.addAttribute("errorMessage", "You do not have permission to access that page.");
        }
        
        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }
}