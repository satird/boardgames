package ru.satird.boardgame.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "bgg_id")
    private Boardgame boardgame;
    @Column(updatable = false)
    private Date creationDate;

    public Comment() {
    }

    public Comment(String text, User author, Boardgame boardgame, Date creationDate) {
        this.text = text;
        this.author = author;
        this.boardgame = boardgame;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Boardgame getBoardgame() {
        return boardgame;
    }

    public void setBoardgame(Boardgame boardgame) {
        this.boardgame = boardgame;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
