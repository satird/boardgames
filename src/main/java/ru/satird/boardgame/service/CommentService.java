package ru.satird.boardgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.Comment;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.repository.CommentRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Iterable<Comment> findAllByBoardgame(Boardgame detailGame) {
        return commentRepository.findAllByBoardgameOrderById(detailGame);
    }

    public void save(Comment commentMessage) {
        commentRepository.save(commentMessage);
    }

    public Comment findById(Long id) {
        return commentRepository.findCommentById(id);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
