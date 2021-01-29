package ru.satird.boardgame.repository;

import org.springframework.data.repository.CrudRepository;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Iterable<Comment> findAllByBoardgame(Boardgame boardgame);
}
