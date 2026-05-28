package ru.aviation.logbook.web.chief;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.aviation.logbook.service.PilotAccountService;

@Controller
@RequestMapping("/chief")
@PreAuthorize("hasRole('CHIEF')")
public class ChiefDashboardController {

    private final PilotAccountService pilotAccountService;

    public ChiefDashboardController(PilotAccountService pilotAccountService) {
        this.pilotAccountService = pilotAccountService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pilots", pilotAccountService.listPilotSummaries());
        return "chief/index";
    }
}
