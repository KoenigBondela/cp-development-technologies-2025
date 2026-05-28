package ru.aviation.logbook.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.service.CurrentUserService;
import ru.aviation.logbook.service.FlightEntryService;
import ru.aviation.logbook.service.ReferenceDataService;
import ru.aviation.logbook.web.dto.FlightEntryForm;
import ru.aviation.logbook.web.support.FlightFormSupport;

@Controller
@RequestMapping("/flights")
public class PilotFlightController {

    private final CurrentUserService currentUserService;
    private final FlightEntryService flightEntryService;
    private final ReferenceDataService referenceDataService;

    public PilotFlightController(
            CurrentUserService currentUserService,
            FlightEntryService flightEntryService,
            ReferenceDataService referenceDataService) {
        this.currentUserService = currentUserService;
        this.flightEntryService = flightEntryService;
        this.referenceDataService = referenceDataService;
    }

    @GetMapping
    public String list(Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        model.addAttribute("pilot", pilot);
        model.addAttribute("flights", flightEntryService.findForPilot(pilot.getId()));
        model.addAttribute("totalHours", flightEntryService.totalFlightHours(pilot.getId()));
        model.addAttribute("flightForm", FlightFormSupport.defaultForm());
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/list";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("flightForm") FlightEntryForm form,
            BindingResult bindingResult,
            Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        if (bindingResult.hasErrors()) {
            return listWithForm(model, pilot, form);
        }
        try {
            flightEntryService.create(pilot, form);
            return "redirect:/flights";
        } catch (IllegalArgumentException e) {
            model.addAttribute("formError", e.getMessage());
            return listWithForm(model, pilot, form);
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        FlightEntryForm form = new FlightEntryForm();
        FlightFormSupport.copyEntryToForm(
                flightEntryService.requireForPilot(id, pilot.getId()), form, referenceDataService);
        model.addAttribute("pilot", pilot);
        model.addAttribute("flightForm", form);
        model.addAttribute("flightId", id);
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("flightForm") FlightEntryForm form,
            BindingResult bindingResult,
            Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        if (bindingResult.hasErrors()) {
            model.addAttribute("pilot", pilot);
            model.addAttribute("flightId", id);
            FlightFormSupport.enrichModel(model, referenceDataService);
            return "flights/edit";
        }
        try {
            flightEntryService.update(id, pilot.getId(), form);
            return "redirect:/flights";
        } catch (IllegalArgumentException e) {
            model.addAttribute("pilot", pilot);
            model.addAttribute("flightId", id);
            model.addAttribute("formError", e.getMessage());
            FlightFormSupport.enrichModel(model, referenceDataService);
            return "flights/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        flightEntryService.delete(id, pilot.getId());
        return "redirect:/flights";
    }

    private String listWithForm(Model model, PilotUser pilot, FlightEntryForm form) {
        model.addAttribute("pilot", pilot);
        model.addAttribute("flights", flightEntryService.findForPilot(pilot.getId()));
        model.addAttribute("totalHours", flightEntryService.totalFlightHours(pilot.getId()));
        model.addAttribute("flightForm", form);
        FlightFormSupport.enrichModel(model, referenceDataService);
        return "flights/list";
    }
}
