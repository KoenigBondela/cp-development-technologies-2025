package com.hoteldb.spring.web;

import com.hoteldb.spring.service.UserManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final UserManagementService userManagementService;

    public WelcomeController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/welcome")
    public String welcome(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (admin) {
            return "redirect:/admin";
        }
        return "redirect:/welcome/user";
    }

    @GetMapping("/welcome/user")
    @PreAuthorize("hasRole('USER')")
    public String welcomeUser(Model model, Authentication authentication) {
        model.addAttribute("me", userManagementService.requireActiveByUsername(authentication.getName()));
        return "welcome-user";
    }
}
