package ru.satird.boardgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.satird.boardgame.controller.utils.ControllerUtils;
import ru.satird.boardgame.domain.*;
import ru.satird.boardgame.service.BoardgameService;
import ru.satird.boardgame.service.CommentService;
import ru.satird.boardgame.service.UserService;

import java.util.*;


@Controller
@RequestMapping(value = "boardgames")
public class BoardGameController {

    @Autowired
    private final BoardgameService boardgameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    public BoardGameController(BoardgameService boardgameRepository) {
        this.boardgameService = boardgameRepository;
    }


    @ModelAttribute("user")
    public User userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @GetMapping()
    public String main(
            @RequestParam(required = false, defaultValue = "") String gameName,
            @PageableDefault(sort = {"bggId"}, direction = Sort.Direction.ASC, value = 100) Pageable pageable,
            @RequestParam(required = false) String goToPage,
            @RequestParam(required = false) Integer pageSize,
            Model model) {
        Page<Boardgame> page;
        boolean disable = false;
        String url = "/boardgames?";

        if (gameName != null && !gameName.isEmpty()) {
            page = boardgameService.findByAlternateIgnoreCaseContaining(gameName, pageable);
            url = "/boardgames?gameName=" + gameName + "&";
        } else {
            page = boardgameService.findAll(pageable);
        }
        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }

        Pageable goToNumPage;
        if (goToPage != null && !goToPage.isEmpty()) {
            if (pageSize != null) {
                goToNumPage = PageRequest.of(Integer.parseInt(goToPage) - 1, pageSize, Sort.by("bggId").ascending());
            } else {
                goToNumPage = PageRequest.of(Integer.parseInt(goToPage) - 1, page.getSize(), Sort.by("bggId").ascending());
            }
            if (gameName != null && !gameName.isEmpty()) {
                url = "/boardgames?gameName=" + gameName + "&";
                page = boardgameService.findByAlternateIgnoreCaseContaining(gameName, goToNumPage);
            } else {
                page = boardgameService.findAll(goToNumPage);
            }
        }
        int[] body = ControllerUtils.pagination(page);
        model.addAttribute("body", body);
        model.addAttribute("page", page);
        model.addAttribute("filter", gameName);
        model.addAttribute("url", url);
        model.addAttribute("pageSize", pageable.getPageSize());
        return "boardgames";
    }

    @GetMapping("{id}")
    public String detailBg(
            @PathVariable Long id,
            @RequestParam(required = false) Long comment,
            Model model
    ) {
        Boardgame detailGame = boardgameService.findByBggId(id);
        Iterable<Comment> gameComments = commentService.findAllByBoardgame(detailGame);
        boolean isFavorites;
        if (detailGame.getFavorites().contains(userInfo())) {
            isFavorites = true;
        } else {
            isFavorites = false;
        }
        model.addAttribute("comment", commentService.findById(comment));
        model.addAttribute("isFavorites", isFavorites);
        model.addAttribute("commentMessages", gameComments);
        model.addAttribute("boardgame", boardgameService.findByBggId(id));
        return "bgDetail";
    }

    @PostMapping("{boardgame}/create")
    public String addComment(
            @PathVariable Boardgame boardgame,
            @RequestParam("newComment") String newComment,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Date dateTime = new Date();
        Comment commentMessage = new Comment();
        commentMessage.setText(newComment);
        commentMessage.setAuthor(user);
        commentMessage.setBoardgame(boardgame);
        commentMessage.setCreationDate(dateTime);
        commentService.save(commentMessage);

        return "redirect:/boardgames/" + boardgame.getBggId();
    }

    @PostMapping("{boardgame}/update")
    public String updateComment(
            @PathVariable Boardgame boardgame,
            @RequestParam() Long commentId,
            @RequestParam("editComment") String newComment
    ) {
        Date dateTime = new Date();
        Comment comment = commentService.findById(commentId);
        comment.setText(newComment);
        comment.setCreationDate(dateTime);
        commentService.save(comment);
        return "redirect:/boardgames/" + boardgame.getBggId();
    }

    @PostMapping("{boardgame}/delete")
    public String deleteComment(
            @PathVariable Boardgame boardgame,
            @RequestParam() Long commentId
    ) {
        commentService.delete(commentId);
        return "redirect:/boardgames/" + boardgame.getBggId();
    }

}
