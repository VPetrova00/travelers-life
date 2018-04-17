package org.softuni.myfinalproject.controllers;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String index(Model model) throws IOException {
        model.addAttribute("view", "home/index");
        List<Post> posts = this.postRepository.findAll();

        String encodedImage = "";
        for (Post post : posts) {
            BufferedImage image = ImageIO.read(new File(post.getImagePath()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] res = baos.toByteArray();
            encodedImage = Base64.getEncoder().encodeToString(res);
            post.setImagePath(encodedImage);
        }

        model.addAttribute("allPosts", posts);
        return "base-layout";
    }

    @GetMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
