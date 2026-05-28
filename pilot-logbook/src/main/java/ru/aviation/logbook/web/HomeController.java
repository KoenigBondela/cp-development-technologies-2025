package ru.aviation.logbook.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.aviation.logbook.domain.UserRole;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CHIEF"))) {
            return "redirect:/chief";
        }
        return "redirect:/flights";
    }
}
