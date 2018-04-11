package org.softuni.myfinalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile/upload")
    public String upload(Model model) {
        model.addAttribute("view", "profile/upload");

        return "base-layout";
    }
}
