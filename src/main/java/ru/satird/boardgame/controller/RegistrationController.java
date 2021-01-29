package ru.satird.boardgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.satird.boardgame.controller.utils.ControllerUtils;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.service.UserService;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

//    @ModelAttribute("user")
//    public User userInfo() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findByUsername(authentication.getName()) ;
//        if( user != null) {
//            return user;
//        } else {
//            return null;
//        }
//    }


    @GetMapping("/registration")
    public String registration(
            Model model
    )
    {
//        if (userInfo() != null && userInfo().isAdmin()) {
//            model.addAttribute("role", true);
//        }
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", true);
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password are different!");
            return "registration";
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("message", "Такой пользователь уже существует");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(
            Model model,
            @PathVariable String code
    ) {
        boolean isActivated = userService.activateUser(code);

//        if (userInfo() != null && userInfo().isAdmin()) {
//            model.addAttribute("role", true);
//        }

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
