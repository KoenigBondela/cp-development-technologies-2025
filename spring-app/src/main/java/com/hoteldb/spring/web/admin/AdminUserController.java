package com.hoteldb.spring.web.admin;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.service.UserManagementService;
import com.hoteldb.spring.web.dto.UserForm;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserManagementService userManagementService;

    public AdminUserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userManagementService.findAllActive());
        model.addAttribute("userForm", new UserForm());
        return "admin/users";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("userForm") UserForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userManagementService.findAllActive());
            return "admin/users";
        }
        try {
            userManagementService.create(form);
            return "redirect:/admin/users";
        } catch (IllegalStateException e) {
            model.addAttribute("users", userManagementService.findAllActive());
            model.addAttribute("createError", "Пользователь с таким именем уже существует.");
            return "admin/users";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        UserEntity user = userManagementService.requireActiveById(id);
        UserForm form = new UserForm();
        form.setUsername(user.getUsername());
        form.setPassword(user.getPassword());
        form.setRole(user.getRole());
        model.addAttribute("userForm", form);
        model.addAttribute("userId", id);
        return "admin/user-edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("userForm") UserForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            return "admin/user-edit";
        }
        try {
            userManagementService.update(id, form);
            return "redirect:/admin/users";
        } catch (IllegalStateException e) {
            model.addAttribute("userId", id);
            model.addAttribute("updateError", "Имя пользователя уже занято.");
            return "admin/user-edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String softDelete(@PathVariable("id") Integer id) {
        userManagementService.softDelete(id);
        return "redirect:/admin/users";
    }
}
