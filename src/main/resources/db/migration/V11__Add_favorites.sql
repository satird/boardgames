CREATE TABLE favorites (
    user_id BIGINT NOT NULL REFERENCES usr,
    boardgame_id BIGINT NOT NULL REFERENCES bgg,
    PRIMARY KEY (user_id, boardgame_id)
)