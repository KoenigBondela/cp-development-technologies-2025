package com.hoteldb.spring.web.admin;

import com.hoteldb.spring.domain.RoomEntity;
import com.hoteldb.spring.service.RoomManagementService;
import com.hoteldb.spring.web.dto.RoomForm;
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
@RequestMapping("/admin/rooms")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final RoomManagementService roomManagementService;

    public AdminRoomController(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomManagementService.findAll());
        model.addAttribute("roomForm", new RoomForm());
        return "admin/rooms";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("roomForm") RoomForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomManagementService.findAll());
            return "admin/rooms";
        }
        try {
            roomManagementService.create(form);
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            model.addAttribute("rooms", roomManagementService.findAll());
            model.addAttribute("createError", e.getMessage());
            return "admin/rooms";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        RoomEntity room = roomManagementService.requireById(id);
        RoomForm form = new RoomForm();
        form.setRoomNumber(room.getRoomNumber());
        form.setRoomType(room.getRoomType());
        form.setPricePerNight(room.getPricePerNight());
        form.setAvailable(Boolean.TRUE.equals(room.getIsAvailable()));
        model.addAttribute("roomForm", form);
        model.addAttribute("roomId", id);
        return "admin/room-edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("roomForm") RoomForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roomId", id);
            return "admin/room-edit";
        }
        roomManagementService.update(id, form);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        roomManagementService.delete(id);
        return "redirect:/admin/rooms";
    }
}
