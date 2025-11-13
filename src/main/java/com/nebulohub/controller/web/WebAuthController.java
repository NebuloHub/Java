package com.nebulohub.controller.web;

import com.nebulohub.domain.user.CreateUserDto;
import com.nebulohub.exception.DuplicateEntryException;
import com.nebulohub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web Controller for serving the Login page.
 * **UPDATED:** Now handles Login AND Register.
 */
@Controller
@RequiredArgsConstructor // Usei @RequiredArgsConstructor em vez de @Autowired
public class WebAuthController {

    private final UserService userService; // Injetado via construtor

    @GetMapping("/login")
    public String loginForm(
            Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "forbidden", required = false) String forbidden,
            @RequestParam(value = "registerSuccess", required = false) String registerSuccess // Mensagem de sucesso do registro
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
        // **NOVO** Pega a mensagem de sucesso do flash attribute
        if (registerSuccess != null) {
            model.addAttribute("successMessage", "Account created successfully! Please log in.");
        }

        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }

    /**
     * **NEW METHOD**
     * Exibe o formulário de registro.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        // Adiciona um DTO vazio ao model para o th:object do formulário
        model.addAttribute("newUser", new CreateUserDto("", "", ""));
        model.addAttribute("pageTitle", "Register");
        return "auth/register"; // Aponta para o novo template
    }

    /**
     * **NEW METHOD**
     * Processa a submissão do formulário de registro.
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("newUser") CreateUserDto dto,
            BindingResult bindingResult, // Pega erros de validação
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // 1. Se a validação falhar, retorna à página de registro com os erros
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Register");
            return "auth/register";
        }

        try {
            // 2. Tenta criar o usuário
            userService.create(dto);

            // 3. Se for bem-sucedido, redireciona para /login com uma mensagem de sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please log in.");
            return "redirect:/login";

        } catch (DuplicateEntryException e) {
            // 4. Se falhar (ex: email duplicado), retorna ao formulário com um erro global
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("globalError", e.getMessage()); // Exibe o erro no topo
            return "auth/register";
        }
    }
}