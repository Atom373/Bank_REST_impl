package com.example.bankcards.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Value("app.origin")
	private String origin;
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") 
	                .allowedOrigins(origin)
	                .allowedMethods("GET", "POST", "DELETE", "PATCH", "OPTIONS")
	                .allowedHeaders(
	                        "Content-Type",
	                        "Authorization", 
	                        "X-Requested-With",
	                        "Accept"
	                )
	                .allowCredentials(false)
	                .maxAge(3600);
		    }
		};
    }
}
