package org.softuni.myfinalproject.services;

import org.softuni.myfinalproject.models.entities.Role;
import org.softuni.myfinalproject.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public Role getUserRole() {

        Role role = this.findByName("ROLE_USER");

        if (role == null) {
            role = new Role("ROLE_USER");
            this.roleRepository.saveAndFlush(role);
        }

        return role;
    }
}
