package org.softuni.myfinalproject.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.myfinalproject.models.entities.Comment;
import org.softuni.myfinalproject.models.entities.Forumicle;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.CommentModel;
import org.softuni.myfinalproject.models.viewModels.ForumicleModel;
import org.softuni.myfinalproject.repositories.CommentRepository;
import org.softuni.myfinalproject.repositories.ForumRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ForumController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/forum")
    public String forumView(Model model) {
        model.addAttribute("view", "forum/forum");
        List<Forumicle> forumicles = this.forumRepository.findAll();
        model.addAttribute("forumicles", forumicles);

        return "base-layout";
    }

    @GetMapping("/add/forumicle")
    public String addForumicle(Model model) {
        model.addAttribute("view", "forum/addForumicle");
        model.addAttribute("forumicleModel", new ForumicleModel());

        return "base-layout";
    }

    @PostMapping("/add/forumicle")
    public String addForumicle(@Valid ForumicleModel forumicleModel) {
        Forumicle forumicle = new Forumicle();
        this.modelMapper.map(forumicleModel, forumicle);
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByUsername(user.getUsername());
        forumicle.setAuthor(userEntity);
        forumicle.setAuthorUsername(userEntity.getUsername());

        this.forumRepository.saveAndFlush(forumicle);
        return "redirect:/forum";
    }

    @GetMapping("/forumicle/details/{id}")
    public String forumicleDetails(@PathVariable Long id, Model model) {
        if (!this.forumRepository.existsById(id)) {
            return "redirect:/forum";
        }

        Forumicle forumicle = this.forumRepository.getFirstById(id);
        String authorUsername = forumicle.getAuthorUsername();
        List<Comment> comments = forumicle.getComments();

        model.addAttribute("authorUsername", authorUsername);
        model.addAttribute("forumicleDetailsModel", forumicle);
        model.addAttribute("view", "forum/forumicleDetails");

        model.addAttribute("comments", comments);

        return "base-layout";
    }
}
