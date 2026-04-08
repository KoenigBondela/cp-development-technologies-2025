package com.hoteldb.spring.web;

import com.hoteldb.spring.service.RoomQueryService;
import com.hoteldb.spring.service.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final RoomQueryService roomQueryService;
    private final UserAccountService userAccountService;

    public WelcomeController(RoomQueryService roomQueryService, UserAccountService userAccountService) {
        this.roomQueryService = roomQueryService;
        this.userAccountService = userAccountService;
    }

    @GetMapping("/welcome")
    public String welcome(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean admin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        if (admin) {
            model.addAttribute("rooms", roomQueryService.findAll());
        } else {
            model.addAttribute("me", userAccountService.requireByUsername(authentication.getName()));
        }
        return "welcome";
    }
}
