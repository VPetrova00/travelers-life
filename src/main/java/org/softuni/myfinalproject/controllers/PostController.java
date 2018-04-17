package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.PostModel;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;

@Controller
public class PostController {

    private UserRepository userRepository;

    private PostRepository postRepository;

    @Autowired
    public PostController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/posts/create")
    @PreAuthorize("isAuthenticated()")
    public String createPost(Model model) {
        model.addAttribute("view", "posts/createPost");
        model.addAttribute("viewModel", new PostModel());

        return "base-layout";
    }

    @PostMapping("/posts/create")
    @PreAuthorize("isAuthenticated()")
    public String createPost(PostModel postModel, HttpServletRequest httpServletRequest) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByUsername(user.getUsername());

        String imagePath = "C:\\Users\\User\\Pictures\\photos\\";

        Set<String> parameterNames = httpServletRequest.getParameterMap().keySet();
        String imageName = "";

        for (String parameterName : parameterNames) {
            if (parameterName.equals("imagePath")) {
                imageName = httpServletRequest.getParameter(parameterName);
            }
        }

        String wholeImagePathName = imagePath + imageName;
        String debug = "";

        Post post = new Post();
        post.setAuthor(userEntity);
        post.setLatitude(postModel.getLatitude());
        post.setLongitude(postModel.getLongitude());
        post.setTitle(postModel.getTitle());
        post.setContent(postModel.getStory());
        post.setImagePath(wholeImagePathName);

        this.postRepository.saveAndFlush(post);

        return "redirect:/users/profile";
    }
}
