package org.softuni.myfinalproject.repositories;

import org.softuni.myfinalproject.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    Post findFirstById(Long id);

    List<Post> findByAuthorId(Long authorId);

    @Override
    List<Post> findAll();
}
