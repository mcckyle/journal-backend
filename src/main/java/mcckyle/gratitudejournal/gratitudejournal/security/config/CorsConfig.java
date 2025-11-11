//***************************************************************************************
//
//     Filename: CorsConfig.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
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
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "*"));
        corsConfig.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}

//***************************************************************************************
