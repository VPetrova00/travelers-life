package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    private PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/posts/create")
    @PreAuthorize("isAuthenticated()")
    public String createPost(Model model) {
        model.addAttribute("view", "posts/createPost");

        return "base-layout";
    }
}
