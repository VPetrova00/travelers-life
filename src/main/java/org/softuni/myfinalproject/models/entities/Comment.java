package org.softuni.myfinalproject.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    private String authorUsername;

    @ManyToOne(targetEntity = User.class)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forumicle_id", nullable = false)
    private Forumicle forumicle;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Forumicle getForumicle() {
        return forumicle;
    }

    public void setForumicle(Forumicle forumicle) {
        this.forumicle = forumicle;
    }
}
