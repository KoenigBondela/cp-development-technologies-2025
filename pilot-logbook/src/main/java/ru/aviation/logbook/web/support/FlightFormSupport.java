package ru.aviation.logbook.web.support;

import org.springframework.ui.Model;
import ru.aviation.logbook.domain.DayNight;
import ru.aviation.logbook.domain.FlightEntry;
import ru.aviation.logbook.domain.FlightRoute;
import ru.aviation.logbook.service.FlightEntryService;
import ru.aviation.logbook.service.ReferenceDataService;
import ru.aviation.logbook.web.dto.FlightEntryForm;

import java.time.LocalDate;
import java.util.List;

public final class FlightFormSupport {

    private FlightFormSupport() {
    }

    public static FlightEntryForm defaultForm() {
        FlightEntryForm form = new FlightEntryForm();
        form.setFlightDate(LocalDate.now());
        form.setDayNight(DayNight.DAY);
        return form;
    }

    public static void enrichModel(Model model, ReferenceDataService referenceDataService) {
        model.addAttribute("aircraftList", referenceDataService.activeAircraft());
        model.addAttribute("routeList", referenceDataService.activeRoutes());
        model.addAttribute("dayNightOptions", DayNight.values());
    }

    public static void resolveRouteIdOnEdit(FlightEntry entry, FlightEntryForm form, List<FlightRoute> routes) {
        if (entry.getAircraft() != null) {
            form.setAircraftId(entry.getAircraft().getId());
        }
        routes.stream()
                .filter(r -> r.getLabel().equals(entry.getRoute()))
                .findFirst()
                .ifPresent(r -> form.setRouteId(r.getId()));
    }

    public static void copyEntryToForm(
            FlightEntry entry,
            FlightEntryForm form,
            ReferenceDataService referenceDataService) {
        FlightEntryService.copyToForm(entry, form);
        resolveRouteIdOnEdit(entry, form, referenceDataService.activeRoutes());
    }
}
