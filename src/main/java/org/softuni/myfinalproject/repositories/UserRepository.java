package org.softuni.myfinalproject.repositories;

import org.softuni.myfinalproject.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Override
    User getOne(Long id);

    boolean existsByUsername(String username);
}
