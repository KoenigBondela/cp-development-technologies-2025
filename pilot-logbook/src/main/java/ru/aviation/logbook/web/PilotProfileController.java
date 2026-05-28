package ru.aviation.logbook.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.service.CurrentUserService;
import ru.aviation.logbook.service.PilotAccountService;
import ru.aviation.logbook.web.dto.PilotProfileForm;

@Controller
@RequestMapping("/profile")
public class PilotProfileController {

    private final CurrentUserService currentUserService;
    private final PilotAccountService pilotAccountService;

    public PilotProfileController(
            CurrentUserService currentUserService,
            PilotAccountService pilotAccountService) {
        this.currentUserService = currentUserService;
        this.pilotAccountService = pilotAccountService;
    }

    @GetMapping
    public String profile(Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        PilotProfileForm form = new PilotProfileForm();
        PilotAccountService.copyToProfileForm(pilot, form);
        model.addAttribute("pilot", pilot);
        model.addAttribute("profileForm", form);
        return "profile/index";
    }

    @PostMapping
    public String update(
            @Valid @ModelAttribute("profileForm") PilotProfileForm form,
            BindingResult bindingResult,
            Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        if (bindingResult.hasErrors()) {
            model.addAttribute("pilot", pilot);
            return "profile/index";
        }
        try {
            pilotAccountService.updateOwnProfile(pilot, form);
            return "redirect:/profile?saved";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("profileError", e.getMessage());
            model.addAttribute("pilot", pilot);
            return "profile/index";
        }
    }

    @PostMapping("/password")
    public String changePassword(
            @RequestParam String newPassword,
            @RequestParam String newPasswordConfirm,
            Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        if (newPassword == null || newPassword.length() < 4) {
            model.addAttribute("passwordError", "Пароль должен быть не короче 4 символов");
            return profile(model);
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return profile(model);
        }
        pilotAccountService.changePassword(pilot, newPassword);
        return "redirect:/profile?passwordChanged";
    }
}
