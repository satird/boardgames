package ru.satird.boardgame.repository;

import org.springframework.data.repository.CrudRepository;
import ru.satird.boardgame.domain.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
