package com.marketplace.controller.user;

import com.marketplace.repository.user.DbUser;
import com.marketplace.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new DbUser());
        return "register";
    }

    @PostMapping("register")
    public void register(@ModelAttribute DbUser user, HttpServletRequest request) throws ServletException {
        service.addUser(user);
        request.login(user.getUsername(), user.getPassword());

    }
}
