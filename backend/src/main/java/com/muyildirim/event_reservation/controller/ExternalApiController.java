package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.service.ExternalQuoteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class ExternalApiController {

    private final ExternalQuoteService externalQuoteService;

    public ExternalApiController(ExternalQuoteService externalQuoteService) {
        this.externalQuoteService = externalQuoteService;
    }

    @GetMapping("/quote")
    public Map<String, Object> quote() {
        return externalQuoteService.getRandomQuote();
    }
}
