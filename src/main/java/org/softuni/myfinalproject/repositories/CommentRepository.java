package org.softuni.myfinalproject.repositories;

import org.softuni.myfinalproject.models.entities.Comment;
import org.softuni.myfinalproject.models.entities.Forumicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
