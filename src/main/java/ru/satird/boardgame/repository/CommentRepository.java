package ru.satird.boardgame.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    Iterable<Comment> findAllByBoardgameOrderById(Boardgame boardgame);
    Comment findCommentById(Long id);
}
