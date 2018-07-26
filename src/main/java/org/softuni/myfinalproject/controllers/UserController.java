package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserLoginModel;
import org.softuni.myfinalproject.models.viewModels.UserRegistrationModel;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.softuni.myfinalproject.services.NotificationService;
import org.softuni.myfinalproject.services.UserService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/users/register")
    public String register(@ModelAttribute UserRegistrationModel viewModel, Model model) {
        model.addAttribute("view", "users/register");

        if (!model.containsAttribute("viewModel")) {
            model.addAttribute("viewModel", new UserRegistrationModel());
        }

        return "base-layout";
    }

    @PostMapping("/users/register")
    public String register(@Valid @ModelAttribute UserRegistrationModel viewModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", viewModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);
            this.notificationService.addErrorMessage("Errors when registering");
            return "redirect:/users/register";
        }

        if (!viewModel.getPassword().equals(viewModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", viewModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationModel", bindingResult);
            this.notificationService.addErrorMessage("Password and confirm password don't match");
            return "redirect:/users/register";
        }

        this.userService.register(viewModel);

        notificationService.addInfoMessage("Register successful");
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
    public String userProfile(Model model) throws IOException {
        this.userService.loadProfile(model);
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

    @GetMapping("/users/profile/{username}")
    public String getProfileByUsername(@PathVariable String username, Model model) throws IOException {
        if (!this.userService.existsByUsername(username)) {
            return "redirect:/users/profile";
        }

        this.userService.loadProfileByUsername(username, model);

        return "base-layout";
    }
}
