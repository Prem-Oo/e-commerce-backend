package com.catalogue.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from the specified origins
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        
        // Allow specific HTTP methods (e.g., GET, POST, PUT, DELETE)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        
        // Allow specific headers
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        
        // Allow credentials (e.g., cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Set the maximum age of a preflight request (in seconds)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}

