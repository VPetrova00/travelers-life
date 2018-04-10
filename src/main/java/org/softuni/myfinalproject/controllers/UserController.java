package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserLoginModel;
import org.softuni.myfinalproject.models.viewModels.UserRegistrationModel;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.softuni.myfinalproject.services.BlogUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogUserDetailsService blogUserDetailsService;

    @GetMapping("/users/register")
    public String register(Model model) {
        model.addAttribute("view", "users/register");

        if (!model.containsAttribute("viewModel")) {
            model.addAttribute("viewModel", new UserRegistrationModel());
        }

        return "base-layout";
    }

    @PostMapping("/users/register")
    public String register(@Valid UserRegistrationModel viewModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", viewModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);
            return "redirect:/users/register";
        }

        this.blogUserDetailsService.register(viewModel);

        return "redirect:/users/login";
    }

    @GetMapping("/users/login")
    public String login(Model model) {
        model.addAttribute("view", "users/login");
        model.addAttribute("viewModel", new UserLoginModel());
        return "base-layout";
    }

    @GetMapping("/users/profile")
    @PreAuthorize("isAuthenticated()")
    public String userProfile(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByUsername(principal.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("view", "users/profile");

        return "base-layout";
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        }

        return "redirect:/users/login?logout";
    }
}
