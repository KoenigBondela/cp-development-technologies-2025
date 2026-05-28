package ru.aviation.logbook.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.aviation.logbook.service.PilotAccountService;
import ru.aviation.logbook.web.dto.PilotRegisterForm;

@Controller
public class RegisterController {

    private final PilotAccountService pilotAccountService;

    public RegisterController(PilotAccountService pilotAccountService) {
        this.pilotAccountService = pilotAccountService;
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("registerForm", new PilotRegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerForm") PilotRegisterForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "mismatch", "Пароли не совпадают");
            return "auth/register";
        }
        try {
            pilotAccountService.registerPilot(form);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("registerError", e.getMessage());
            return "auth/register";
        }
    }
}
