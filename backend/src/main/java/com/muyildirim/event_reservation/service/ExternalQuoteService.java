package com.muyildirim.event_reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ExternalQuoteService {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔥 HTTPS değil HTTP → SSL problemi YOK
    private static final String QUOTE_URL = "http://api.quotable.io/random";

    public Map<String, Object> getRandomQuote() {
        return restTemplate.getForObject(QUOTE_URL, Map.class);
    }
}
