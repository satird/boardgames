package ru.satird.boardgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.satird.boardgame.domain.Boardgame;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.service.BoardgameService;
import ru.satird.boardgame.service.CommentService;
import ru.satird.boardgame.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("favorites")
public class FavoriteController {

    @Autowired
    private BoardgameService boardgameService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;


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

    @GetMapping("{boardgame}/bookmark")
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

    @GetMapping("")
    public String favorites(
            Model model
    ) {
        Iterable<Boardgame> boardgames;

        boardgames = boardgameService.findByFavorites(userInfo());
        model.addAttribute("boardgames", boardgames);

        return "favorites";
    }
}
