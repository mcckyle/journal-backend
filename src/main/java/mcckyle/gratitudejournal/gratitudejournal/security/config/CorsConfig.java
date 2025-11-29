//***************************************************************************************
//
//     Filename: CorsConfig.java
//     Author: Kyle McColgan
//     Date: 28 November 2025
//     Description: This file implements a custom CORS config bean for security.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//***************************************************************************************

@Configuration
public class CorsConfig
{
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "https://journal-backend-vnla.onrender.com" //Replace with real URL.
        ));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        corsConfig.setExposedHeaders(List.of(
                "Set-Cookie",
                "Authorization",
                "Content-Type"
        ));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}

//***************************************************************************************
