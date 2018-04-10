package org.softuni.myfinalproject.services;

import org.modelmapper.ModelMapper;
import org.softuni.myfinalproject.models.entities.Role;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserRegistrationModel;
import org.softuni.myfinalproject.repositories.RoleRepository;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service("projectUserDetailsService")
public class BlogUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final RoleRepository roleRepository;

    private final RoleService roleService;

    @Autowired
    public BlogUserDetailsService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    public void register(UserRegistrationModel userRegistrationModel) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User();

        this.modelMapper.map(userRegistrationModel, user);

        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationModel.getPassword()));

//        Role role = this.roleService.getUserRole();
//
//        user.addRole(role);

        //TODO: set roles to users!!!

        this.userRepository.saveAndFlush(user);
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
}
