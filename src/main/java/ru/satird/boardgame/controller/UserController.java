package ru.satird.boardgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.satird.boardgame.domain.AuthenticationProvider;
import ru.satird.boardgame.domain.Role;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.service.UserService;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(
            Model model
    ) {
        model.addAttribute("users", userService.findAll());
        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }
        return "userList";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
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


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user,
            Model model
            ) {

        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User userItem
    ) {
        userService.saveUser(userItem, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(
            Model model
    ) {
        model.addAttribute("user", userInfo());
        model.addAttribute("avatar", userInfo().getUserpic());
        if (userInfo().getAuthProvider() == AuthenticationProvider.LOCAL || userInfo().getAuthProvider() == null) {
            model.addAttribute("visible", true);
        }
        if (userInfo() != null && userInfo().isAdmin()) {
            model.addAttribute("role", true);
        }
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            Model model,
            @RequestParam String password,
            @RequestParam(name = "userpic", required = false) MultipartFile userpic
    ) throws IOException {
        if (userpic == null || userpic.isEmpty()) {
            userService.updatePassword(userInfo(), password);
            System.out.println("Картинка не загружена");
        }
        userService.updateProfile(userInfo(), password, userpic);
        model.addAttribute("avatar", userInfo().getUserpic());
        return "redirect:/user/profile";
    }


}
