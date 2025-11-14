//***************************************************************************************
//
//     Filename: User.java
//     Author: Kyle McColgan
//     Date: 14 November 2025
//     Description: This file holds the auth filter configuration.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.config;

import mcckyle.gratitudejournal.gratitudejournal.security.JwtAuthenticationFilter;
import mcckyle.gratitudejournal.gratitudejournal.security.UserDetailsServiceImpl;
import mcckyle.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//***************************************************************************************

@Configuration
public class FilterConfig
{
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService)
    {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }
}

//***************************************************************************************
