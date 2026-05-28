package ru.aviation.logbook.web.chief;

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
import org.springframework.web.bind.annotation.RequestParam;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.service.PilotAccountService;
import ru.aviation.logbook.web.dto.PilotProfileForm;

@Controller
@RequestMapping("/chief/pilots")
@PreAuthorize("hasRole('CHIEF')")
public class ChiefPilotController {

    private final PilotAccountService pilotAccountService;

    public ChiefPilotController(PilotAccountService pilotAccountService) {
        this.pilotAccountService = pilotAccountService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pilots", pilotAccountService.listPilotSummaries());
        return "chief/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("profileForm", new PilotProfileForm());
        model.addAttribute("creating", true);
        return "chief/pilot-edit";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute("profileForm") PilotProfileForm form,
            BindingResult bindingResult,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("creating", true);
            return "chief/pilot-edit";
        }
        if (!password.equals(passwordConfirm)) {
            bindingResult.reject("passwordMismatch", "Пароли не совпадают");
            model.addAttribute("creating", true);
            return "chief/pilot-edit";
        }
        try {
            pilotAccountService.createPilotByChief(form, password);
            return "redirect:/chief/pilots";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("profileError", e.getMessage());
            model.addAttribute("creating", true);
            return "chief/pilot-edit";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(id);
        model.addAttribute("pilot", pilot);
        model.addAttribute("totalHours", pilotAccountService.totalHoursForPilot(id));
        return "chief/pilot-detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(id);
        PilotProfileForm form = new PilotProfileForm();
        PilotAccountService.copyToProfileForm(pilot, form);
        model.addAttribute("pilot", pilot);
        model.addAttribute("profileForm", form);
        return "chief/pilot-edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("profileForm") PilotProfileForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pilot", pilotAccountService.requirePilot(id));
            return "chief/pilot-edit";
        }
        try {
            pilotAccountService.updatePilotProfile(id, form);
            return "redirect:/chief/pilots/" + id;
        } catch (IllegalArgumentException e) {
            bindingResult.reject("profileError", e.getMessage());
            model.addAttribute("pilot", pilotAccountService.requirePilot(id));
            return "chief/pilot-edit";
        }
    }

    @PostMapping("/{id}/password")
    public String resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword,
            @RequestParam String newPasswordConfirm) {
        if (!newPassword.equals(newPasswordConfirm) || newPassword.length() < 4) {
            return "redirect:/chief/pilots/" + id + "/edit?passwordError";
        }
        PilotUser pilot = pilotAccountService.requirePilot(id);
        pilotAccountService.changePassword(pilot, newPassword);
        return "redirect:/chief/pilots/" + id + "?passwordChanged";
    }
}
