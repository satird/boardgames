package ru.satird.boardgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.Comment;
import ru.satird.boardgame.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Iterable<Comment> findAllByBoardgame(Boardgame detailGame) {
        return commentRepository.findAllByBoardgame(detailGame);
    }

    public void save(Comment commentMessage) {
        commentRepository.save(commentMessage);
    }
}
