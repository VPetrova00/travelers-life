package org.softuni.myfinalproject.services;

import org.modelmapper.ModelMapper;
import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.Role;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserRegistrationModel;
import org.softuni.myfinalproject.repositories.PostRepository;
import org.softuni.myfinalproject.repositories.RoleRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("projectUserDetailsService")
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PostRepository postRepository;

    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, PostRepository postRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.roleService = roleService;
    }

    public void register(UserRegistrationModel userRegistrationModel) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User();

        this.modelMapper.map(userRegistrationModel, user);

        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationModel.getPassword()));

        Role role = this.roleService.getUserRole();
        Role admin = this.roleService.getAdminRole();

        if (this.userRepository.findAll().isEmpty()) {
            user.addRole(admin);
        } else {
            user.addRole(role);
        }

        this.userRepository.saveAndFlush(user);
    }

    public void loadProfile(Model model) throws IOException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByUsername(principal.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("view", "users/profile");

        List<Post> allPosts = this.postRepository.findByAuthorId(user.getId());

        String encodedImage = "";
        for (Post post : allPosts) {
            BufferedImage image = ImageIO.read(new File(post.getImagePath()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] res = baos.toByteArray();
            encodedImage = Base64.getEncoder().encodeToString(res);
            post.setImagePath(encodedImage);
        }

        model.addAttribute("postModels", allPosts);
    }

    public void loadProfileByUsername(String username, Model model) throws IOException {
        List<Post> posts = this.postRepository.findByAuthorUsername(username);

        String encodedImage = "";
        for (Post post : posts) {
            BufferedImage image = ImageIO.read(new File(post.getImagePath()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            byte[] res = baos.toByteArray();
            encodedImage = Base64.getEncoder().encodeToString(res);
            post.setImagePath(encodedImage);
        }

        String userName = this.userRepository.findByUsername(username).getUsername();
        model.addAttribute("view", "users/profile");
        model.addAttribute("username", userName);
        model.addAttribute("postModels", posts);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid User");
        }
        else {
            Set<GrantedAuthority> grantedAuthorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());

            return new org
                    .springframework
                    .security
                    .core
                    .userdetails
                    .User(user.getUsername(), user.getPassword(), grantedAuthorities);
        }
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }
}
