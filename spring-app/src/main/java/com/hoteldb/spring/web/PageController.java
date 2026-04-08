package com.hoteldb.spring.web;

import com.hoteldb.spring.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final UserAccountService userAccountService;

    public PageController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        try {
            userAccountService.registerUser(username, password);
            return "redirect:/login?registered";
        } catch (IllegalStateException e) {
            model.addAttribute("error", "duplicate");
            return "register";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "invalid");
            return "register";
        }
    }
}
