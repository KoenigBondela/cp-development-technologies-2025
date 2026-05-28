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
import ru.aviation.logbook.service.AircraftCatalogService;
import ru.aviation.logbook.service.FlightRouteCatalogService;
import ru.aviation.logbook.web.dto.AircraftForm;
import ru.aviation.logbook.web.dto.FlightRouteForm;

@Controller
@RequestMapping("/chief/catalog")
@PreAuthorize("hasRole('CHIEF')")
public class ChiefCatalogController {

    private final AircraftCatalogService aircraftCatalogService;
    private final FlightRouteCatalogService flightRouteCatalogService;

    public ChiefCatalogController(
            AircraftCatalogService aircraftCatalogService,
            FlightRouteCatalogService flightRouteCatalogService) {
        this.aircraftCatalogService = aircraftCatalogService;
        this.flightRouteCatalogService = flightRouteCatalogService;
    }

    @GetMapping
    public String catalog(Model model) {
        model.addAttribute("aircraftList", aircraftCatalogService.findAll());
        model.addAttribute("routeList", flightRouteCatalogService.findAll());
        model.addAttribute("aircraftForm", new AircraftForm());
        model.addAttribute("routeForm", new FlightRouteForm());
        return "chief/catalog";
    }

    @PostMapping("/aircraft")
    public String createAircraft(
            @Valid @ModelAttribute("aircraftForm") AircraftForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return catalogWithError(model, form, null);
        }
        try {
            aircraftCatalogService.create(form);
            return "redirect:/chief/catalog?aircraftSaved";
        } catch (IllegalArgumentException e) {
            model.addAttribute("aircraftError", e.getMessage());
            return catalogWithError(model, form, null);
        }
    }

    @GetMapping("/aircraft/{id}/edit")
    public String editAircraftForm(@PathVariable Long id, Model model) {
        AircraftForm form = new AircraftForm();
        AircraftCatalogService.copyToForm(aircraftCatalogService.requireById(id), form);
        model.addAttribute("aircraftList", aircraftCatalogService.findAll());
        model.addAttribute("routeList", flightRouteCatalogService.findAll());
        model.addAttribute("aircraftForm", form);
        model.addAttribute("routeForm", new FlightRouteForm());
        model.addAttribute("aircraftEditId", id);
        return "chief/catalog";
    }

    @PostMapping("/aircraft/{id}")
    public String updateAircraft(
            @PathVariable Long id,
            @Valid @ModelAttribute("aircraftForm") AircraftForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("aircraftEditId", id);
            return catalogWithError(model, form, null);
        }
        try {
            aircraftCatalogService.update(id, form);
            return "redirect:/chief/catalog?aircraftSaved";
        } catch (IllegalArgumentException e) {
            model.addAttribute("aircraftError", e.getMessage());
            model.addAttribute("aircraftEditId", id);
            return catalogWithError(model, form, null);
        }
    }

    @PostMapping("/aircraft/{id}/deactivate")
    public String deactivateAircraft(@PathVariable Long id) {
        aircraftCatalogService.deactivate(id);
        return "redirect:/chief/catalog?aircraftSaved";
    }

    @PostMapping("/routes")
    public String createRoute(
            @Valid @ModelAttribute("routeForm") FlightRouteForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return catalogWithError(model, null, form);
        }
        try {
            flightRouteCatalogService.create(form);
            return "redirect:/chief/catalog?routeSaved";
        } catch (IllegalArgumentException e) {
            model.addAttribute("routeError", e.getMessage());
            return catalogWithError(model, null, form);
        }
    }

    @GetMapping("/routes/{id}/edit")
    public String editRouteForm(@PathVariable Long id, Model model) {
        FlightRouteForm form = new FlightRouteForm();
        FlightRouteCatalogService.copyToForm(flightRouteCatalogService.requireById(id), form);
        model.addAttribute("aircraftList", aircraftCatalogService.findAll());
        model.addAttribute("routeList", flightRouteCatalogService.findAll());
        model.addAttribute("aircraftForm", new AircraftForm());
        model.addAttribute("routeForm", form);
        model.addAttribute("routeEditId", id);
        return "chief/catalog";
    }

    @PostMapping("/routes/{id}")
    public String updateRoute(
            @PathVariable Long id,
            @Valid @ModelAttribute("routeForm") FlightRouteForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("routeEditId", id);
            return catalogWithError(model, null, form);
        }
        try {
            flightRouteCatalogService.update(id, form);
            return "redirect:/chief/catalog?routeSaved";
        } catch (IllegalArgumentException e) {
            model.addAttribute("routeError", e.getMessage());
            model.addAttribute("routeEditId", id);
            return catalogWithError(model, null, form);
        }
    }

    @PostMapping("/routes/{id}/deactivate")
    public String deactivateRoute(@PathVariable Long id) {
        flightRouteCatalogService.deactivate(id);
        return "redirect:/chief/catalog?routeSaved";
    }

    private String catalogWithError(Model model, AircraftForm aircraftForm, FlightRouteForm routeForm) {
        model.addAttribute("aircraftList", aircraftCatalogService.findAll());
        model.addAttribute("routeList", flightRouteCatalogService.findAll());
        model.addAttribute("aircraftForm", aircraftForm != null ? aircraftForm : new AircraftForm());
        model.addAttribute("routeForm", routeForm != null ? routeForm : new FlightRouteForm());
        return "chief/catalog";
    }
}
