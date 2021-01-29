package ru.satird.boardgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.satird.boardgame.domain.Message;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.repository.MessageRepository;
import ru.satird.boardgame.service.UserService;

import java.util.Calendar;

@Controller
public class MessagesController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public User userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()) ;
        if( user != null) {
            return user;
        } else {
            return null;
        }
    }

    @GetMapping("/")
    public String home(
            User user,
            Model model
    ) {

        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }
        Calendar today = Calendar.getInstance();
        model.addAttribute("user", user);
        model.addAttribute("today", today);
        return "main";
    }

//    @RequestMapping("/login.html")
//    public String login() {
//        return "login";
//    }
//
//    @RequestMapping("/login?error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "login";
//    }

    @GetMapping("/messages")
    public String main(Model model) {
        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "messages";
    }

    @PostMapping("/messages")
    public String add(
            @RequestParam String text,
            @RequestParam String tag,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()) ;
        Message message = new Message(text, tag, user);
        messageRepository.save(message);

        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);

        return "messages";
    }

}
