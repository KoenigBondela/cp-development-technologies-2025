package com.hoteldb.spring.api;

import com.hoteldb.spring.api.dto.UserResponse;
import com.hoteldb.spring.service.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CurrentUserRestController {

    private final UserAccountService userAccountService;

    public CurrentUserRestController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return UserResponse.from(userAccountService.requireByUsername(authentication.getName()));
    }
}
