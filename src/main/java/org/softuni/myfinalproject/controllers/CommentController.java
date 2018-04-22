package org.softuni.myfinalproject.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.myfinalproject.models.entities.Comment;
import org.softuni.myfinalproject.models.entities.Forumicle;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.CommentModel;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ForumRepository forumRepository;

    @GetMapping("/add/comment/{forumicleId}")
    public String comment(Model model, @PathVariable Long forumicleId) {
        model.addAttribute("commentModel", new CommentModel());
        model.addAttribute("forumicleId", forumicleId);
        model.addAttribute("view", "comments/add");

        return "base-layout";
    }

    @PostMapping("/add/comment/{forumicleId}")
    public String comment(@Valid CommentModel commentModel, @PathVariable Long forumicleId) {
        Comment comment = new Comment();
        this.modelMapper.map(commentModel, comment);
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByUsername(user.getUsername());
        comment.setAuthor(userEntity);
        comment.setAuthorUsername(userEntity.getUsername());
        Forumicle forumicle = this.forumRepository.getOne(forumicleId);
        comment.setForumicle(forumicle);
        this.commentRepository.saveAndFlush(comment);

        return "redirect:/forum";
    }
}
