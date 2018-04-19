package org.softuni.myfinalproject.repositories;

import org.softuni.myfinalproject.models.entities.Post;
import org.softuni.myfinalproject.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CrudRepository<Post, Long> {

    Post findFirstById(Long id);

    List<Post> findByAuthorUsername(String username);

    List<Post> findByAuthorId(Long authorId);

    @Override
    List<Post> findAll();
}
