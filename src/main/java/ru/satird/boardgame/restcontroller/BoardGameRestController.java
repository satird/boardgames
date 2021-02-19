package ru.satird.boardgame.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.Comment;
import ru.satird.boardgame.service.BoardgameService;
import ru.satird.boardgame.service.CommentService;
import ru.satird.boardgame.service.UserService;

@RestController
@RequestMapping(value = "api/comments")
public class BoardGameRestController {

    @Autowired
    private final BoardgameService boardgameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    public BoardGameRestController(BoardgameService boardgameService) {
        this.boardgameService = boardgameService;
    }

    @GetMapping(value = "find/{commentId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> find(
            @PathVariable("commentId") Long commentId
    ) {
        try {
            return new ResponseEntity<Comment>(commentService.findById(commentId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Comment>(HttpStatus.BAD_REQUEST);
        }
    }
}
