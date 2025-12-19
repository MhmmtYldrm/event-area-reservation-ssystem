package com.muyildirim.event_reservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

            // ❌ wildcard YOK
            .allowedHeaders(
                "Content-Type",
                "Authorization",
                "Accept"
            )

            // ❌ credentials KAPALI
            .allowCredentials(false);
    }
}