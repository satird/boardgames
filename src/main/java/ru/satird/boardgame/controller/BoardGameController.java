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

    @GetMapping("/boardgames")
    public String main(
            @RequestParam(required = false, defaultValue = "") String gameName,
            @PageableDefault(sort = {"bggId"}, direction = Sort.Direction.ASC, value = 100) Pageable pageable,
            @RequestParam(required = false) String goToPage,
            Model model) {
        Page<Boardgame> page;
        String url = "/boardgames?";

        if (gameName != null && !gameName.isEmpty()) {
            page = boardgameService.findByPrimaryNameIgnoreCaseContaining(gameName, pageable);
            url = "/boardgames?gameName=" + gameName + "&";
        } else {
            page = boardgameService.findAll(pageable);
        }
        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }

        if (goToPage != null && !goToPage.isEmpty()) {
            Pageable goToNumPage = PageRequest.of(Integer.parseInt(goToPage)-1, 100, Sort.by("bggId").ascending());
            if (gameName != null && !gameName.isEmpty()) {
                url = "/boardgames?gameName=" + gameName + "&";
                page = boardgameService.findByPrimaryNameIgnoreCaseContaining(gameName, goToNumPage);
            } else {
                page = boardgameService.findAll(goToNumPage);
            }
        }

        int[] body;
        if (page.getTotalPages() > 7) {
            int totalPages = page.getTotalPages();
            int pageNumber = page.getNumber()+1;
            int[] head = (pageNumber > 4) ? new int[]{1, -1} : new int[]{1,2,3};
            int[] bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1) ? new int[]{pageNumber-2, pageNumber-1} : new int[]{};
            int[] bodyCenter = (pageNumber > 3 && pageNumber < totalPages - 2) ? new int[]{pageNumber} : new int[]{};
            int[] bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3) ? new int[]{pageNumber+1, pageNumber+2} : new int[]{};
            int[] tail = (pageNumber < totalPages - 3) ? new int[]{-1, totalPages} : new int[] {totalPages-2, totalPages-1, totalPages};
            body = ControllerUtils.merge(head, bodyBefore, bodyCenter, bodyAfter, tail);

        } else {
            body = new int[page.getTotalPages()];
            for (int i = 0; i < page.getTotalPages(); i++) {
                body[i] = 1+i;
            }
        }

        model.addAttribute("body", body);
        model.addAttribute("page", page);
        model.addAttribute("filter", gameName);
        model.addAttribute("url", url);
        return "boardgames";
    }

    @GetMapping("/boardgames/{id}")
    public String detailBg(
            @PathVariable Long id,
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
        model.addAttribute("isFavorites", isFavorites);
        model.addAttribute("commentMessages", gameComments);
        model.addAttribute("boardgame", boardgameService.findByBggId(id));
        return "bgDetail";
    }

    @PostMapping("/boardgames/{boardgame}")
    public String addComment(
            @PathVariable Boardgame boardgame,
            @RequestParam(required = false) String comment,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        
        Date dateTime = new Date();
        Comment commentMessage = new Comment(comment, user, boardgame, dateTime);
        commentService.save(commentMessage);

        Iterable<Comment> comments = commentService.findAllByBoardgame(boardgame);
        model.addAttribute("commentMessages", comments);
        return "redirect:/boardgames/" + boardgame.getBggId();
    }

    @GetMapping("/favorites/{boardgame}/bookmark")
    public String addFavorites(
            @PathVariable Boardgame boardgame,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> favorites = boardgame.getFavorites();
        if (favorites.contains(userInfo())) {
            favorites.remove(userInfo());
        } else {
            favorites.add(userInfo());
        }
        boardgameService.save(boardgame);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

    @GetMapping("/favorites")
    public String favorites(
            Model model
    ) {
        Iterable<Boardgame> boardgames;

        boardgames = boardgameService.findByFavorites(userInfo());
        model.addAttribute("boardgames", boardgames);

        return "favorites";
    }
}
