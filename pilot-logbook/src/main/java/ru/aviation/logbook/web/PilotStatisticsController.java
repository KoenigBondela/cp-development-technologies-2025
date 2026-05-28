package ru.aviation.logbook.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.service.CurrentUserService;
import ru.aviation.logbook.service.FlightStatisticsService;

import java.time.LocalDate;
import java.time.Year;

@Controller
@RequestMapping("/statistics")
public class PilotStatisticsController {

    private final CurrentUserService currentUserService;
    private final FlightStatisticsService flightStatisticsService;

    public PilotStatisticsController(
            CurrentUserService currentUserService,
            FlightStatisticsService flightStatisticsService) {
        this.currentUserService = currentUserService;
        this.flightStatisticsService = flightStatisticsService;
    }

    @GetMapping
    public String statistics(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Model model) {
        PilotUser pilot = currentUserService.requireCurrentUser();
        populateStatistics(model, pilot, from, to, null);
        return "statistics/index";
    }

    public void populateStatistics(
            Model model,
            PilotUser pilot,
            LocalDate from,
            LocalDate to,
            String backUrl) {
        LocalDate periodFrom = from != null ? from : LocalDate.of(Year.now().getValue(), 1, 1);
        LocalDate periodTo = to != null ? to : LocalDate.now();

        model.addAttribute("pilot", pilot);
        model.addAttribute("from", periodFrom);
        model.addAttribute("to", periodTo);
        if (backUrl != null) {
            model.addAttribute("backUrl", backUrl);
        }

        try {
            model.addAttribute("totalHours",
                    flightStatisticsService.totalHoursBetween(pilot.getId(), periodFrom, periodTo));
            model.addAttribute("byAircraftType",
                    flightStatisticsService.hoursByAircraftTypeBetween(pilot.getId(), periodFrom, periodTo));
        } catch (IllegalArgumentException e) {
            model.addAttribute("statsError", e.getMessage());
        }
    }
}
