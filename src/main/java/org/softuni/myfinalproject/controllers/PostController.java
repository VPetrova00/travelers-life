package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.PostModel;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
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

    @GetMapping("/post/details/{id}")
    public String viewPostDetails(Model model, @PathVariable Long id) {
        if (!this.postRepository.existsById(id)) {
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User entityUser = this.userRepository.findByUsername(user.getUsername());

            model.addAttribute("userModel", entityUser);
        }

        Post post = this.postRepository.findFirstById(id);

        String authorUsername = post.getAuthor().getUsername();

        String debug = "";
        model.addAttribute("username", authorUsername);
        model.addAttribute("postDetailsModel", post);
        model.addAttribute("view", "posts/postDetails");

        return "base-layout";
    }
}
