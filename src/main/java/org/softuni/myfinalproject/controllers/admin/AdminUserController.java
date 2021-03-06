package org.softuni.myfinalproject.controllers.admin;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.Role;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserEditModel;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.softuni.myfinalproject.repositories.RoleRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminUserController {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PostRepository postRepository;

    @Autowired
    public AdminUserController(UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = this.userRepository.findAll();

        model.addAttribute("view", "admin/users/users");
        model.addAttribute("users", users);

        return "base-layout";
    }


    @GetMapping("/admin/users/edit/{id}")
    public String editUsers(@PathVariable Long id, Model model) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (user.getUsername().equals(principal.getUsername())) {
            return "redirect:/admin/users";
        }

        List<Role> roles = this.roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "admin/users/edit");

        return "base-layout";
    }

    @PostMapping("/admin/users/edit/{id}")
    public String editUsers(@PathVariable Long id, UserEditModel userEditModel, HttpServletRequest httpServletRequest) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);

        if (!StringUtils.isEmpty(userEditModel.getPassword())) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));
        }

        user.setUsername(userEditModel.getUsername());

        Set<Role> roles = new HashSet<>();

        Set<String> parameterNames = httpServletRequest.getParameterMap().keySet();

        for (String parameterName : parameterNames) {
            if (parameterName.equals("roles")) {
                String roleAsString = httpServletRequest.getParameter(parameterName);
                Long roleId = Long.parseLong(roleAsString);
                Role role = this.roleRepository.getById(roleId);
                roles.add(role);
            }
        }

        user.setRoles(roles);
        this.userRepository.saveAndFlush(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (user.getUsername().equals(principal.getUsername())) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("view", "admin/users/delete");

        return "base-layout";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);

        for (Post post : user.getPosts()) {
            this.postRepository.delete(post);
        }

        this.userRepository.delete(user);

        return "redirect:/admin/users";
    }
}
