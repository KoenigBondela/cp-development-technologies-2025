package com.hoteldb.spring.web;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.service.UserManagementService;
import com.hoteldb.spring.web.dto.UserForm;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@PreAuthorize("hasRole('USER')")
public class UserProfileController {

    private final UserManagementService userManagementService;

    public UserProfileController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/edit")
    public String editForm(Model model, Authentication authentication) {
        UserEntity me = userManagementService.requireActiveByUsername(authentication.getName());
        UserForm form = new UserForm();
        form.setUsername(me.getUsername());
        form.setPassword(me.getPassword());
        form.setRole(me.getRole());
        model.addAttribute("userForm", form);
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String update(
            @Valid @ModelAttribute("userForm") UserForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "profile/edit";
        }
        userManagementService.updateOwnProfile(authentication.getName(), form.getPassword());
        return "redirect:/welcome/user";
    }
}
