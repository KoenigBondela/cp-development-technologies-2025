package com.hoteldb.spring.api;

import com.hoteldb.spring.api.dto.RegisterRequest;
import com.hoteldb.spring.api.dto.UserResponse;
import com.hoteldb.spring.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterRestController {

    private final UserAccountService userAccountService;

    public RegisterRestController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            var saved = userAccountService.registerUser(request.username(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(saved));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
