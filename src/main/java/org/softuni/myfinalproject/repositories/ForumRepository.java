package org.softuni.myfinalproject.repositories;

import org.softuni.myfinalproject.models.entities.Comment;
import org.softuni.myfinalproject.models.entities.Forumicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forumicle, Long> {
    Forumicle getFirstById(Long id);
}
