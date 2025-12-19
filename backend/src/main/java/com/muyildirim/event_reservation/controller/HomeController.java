package com.muyildirim.event_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Event Area Reservation System");
        model.addAttribute("subtitle", "Thymeleaf Server-Side Rendered Page");
        model.addAttribute("message", "This page is rendered by Spring Boot using Thymeleaf.");
        return "index";
    }
}
