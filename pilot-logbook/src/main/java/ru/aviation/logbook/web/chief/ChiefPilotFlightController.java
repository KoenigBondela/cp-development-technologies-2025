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
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.service.FlightEntryService;
import ru.aviation.logbook.service.PilotAccountService;
import ru.aviation.logbook.service.ReferenceDataService;
import ru.aviation.logbook.web.PilotStatisticsController;
import ru.aviation.logbook.web.dto.FlightEntryForm;
import ru.aviation.logbook.web.support.FlightFormSupport;

import java.time.LocalDate;

@Controller
@RequestMapping("/chief/pilots/{pilotId}")
@PreAuthorize("hasRole('CHIEF')")
public class ChiefPilotFlightController {

    private final PilotAccountService pilotAccountService;
    private final FlightEntryService flightEntryService;
    private final ReferenceDataService referenceDataService;
    private final PilotStatisticsController pilotStatisticsController;

    public ChiefPilotFlightController(
            PilotAccountService pilotAccountService,
            FlightEntryService flightEntryService,
            ReferenceDataService referenceDataService,
            PilotStatisticsController pilotStatisticsController) {
        this.pilotAccountService = pilotAccountService;
        this.flightEntryService = flightEntryService;
        this.referenceDataService = referenceDataService;
        this.pilotStatisticsController = pilotStatisticsController;
    }

    @GetMapping("/flights")
    public String flights(@PathVariable Long pilotId, Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(pilotId);
        model.addAttribute("pilot", pilot);
        model.addAttribute("chiefView", true);
        model.addAttribute("flights", flightEntryService.findForPilot(pilotId));
        model.addAttribute("totalHours", flightEntryService.totalFlightHours(pilotId));
        model.addAttribute("flightForm", FlightFormSupport.defaultForm());
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/list";
    }

    @PostMapping("/flights")
    public String create(
            @PathVariable Long pilotId,
            @Valid @ModelAttribute("flightForm") FlightEntryForm form,
            BindingResult bindingResult,
            Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(pilotId);
        if (bindingResult.hasErrors()) {
            return flightsWithForm(model, pilot, form);
        }
        try {
            flightEntryService.create(pilot, form);
            return "redirect:/chief/pilots/" + pilotId + "/flights";
        } catch (IllegalArgumentException e) {
            model.addAttribute("formError", e.getMessage());
            return flightsWithForm(model, pilot, form);
        }
    }

    @GetMapping("/flights/{id}/edit")
    public String editForm(@PathVariable Long pilotId, @PathVariable Long id, Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(pilotId);
        FlightEntryForm form = new FlightEntryForm();
        FlightFormSupport.copyEntryToForm(
                flightEntryService.requireForPilot(id, pilotId), form, referenceDataService);
        model.addAttribute("pilot", pilot);
        model.addAttribute("chiefView", true);
        model.addAttribute("flightForm", form);
        model.addAttribute("flightId", id);
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/edit";
    }

    @PostMapping("/flights/{id}")
    public String update(
            @PathVariable Long pilotId,
            @PathVariable Long id,
            @Valid @ModelAttribute("flightForm") FlightEntryForm form,
            BindingResult bindingResult,
            Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(pilotId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("pilot", pilot);
            model.addAttribute("chiefView", true);
            model.addAttribute("flightId", id);
            FlightFormSupport.enrichModel(model, referenceDataService);
            return "flights/edit";
        }
        try {
            flightEntryService.update(id, pilotId, form);
            return "redirect:/chief/pilots/" + pilotId + "/flights";
        } catch (IllegalArgumentException e) {
            model.addAttribute("pilot", pilot);
            model.addAttribute("chiefView", true);
            model.addAttribute("flightId", id);
            model.addAttribute("formError", e.getMessage());
            FlightFormSupport.enrichModel(model, referenceDataService);
            return "flights/edit";
        }
    }

    @PostMapping("/flights/{id}/delete")
    public String delete(@PathVariable Long pilotId, @PathVariable Long id) {
        flightEntryService.delete(id, pilotId);
        return "redirect:/chief/pilots/" + pilotId + "/flights";
    }

    @GetMapping("/statistics")
    public String statistics(
            @PathVariable Long pilotId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) LocalDate from,
            @org.springframework.web.bind.annotation.RequestParam(required = false) LocalDate to,
            Model model) {
        PilotUser pilot = pilotAccountService.requirePilot(pilotId);
        model.addAttribute("chiefView", true);
        pilotStatisticsController.populateStatistics(
                model, pilot, from, to, "/chief/pilots/" + pilotId);
        return "statistics/index";
    }

    private String flightsWithForm(Model model, PilotUser pilot, FlightEntryForm form) {
        model.addAttribute("pilot", pilot);
        model.addAttribute("chiefView", true);
        model.addAttribute("flights", flightEntryService.findForPilot(pilot.getId()));
        model.addAttribute("totalHours", flightEntryService.totalFlightHours(pilot.getId()));
        model.addAttribute("flightForm", form);
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/list";
    }
}
