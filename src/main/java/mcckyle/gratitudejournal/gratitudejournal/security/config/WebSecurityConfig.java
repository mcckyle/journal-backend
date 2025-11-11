//***************************************************************************************
//
//     Filename: WebSecurityConfig.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file contains the Spring Security application configuration.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//***************************************************************************************

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
    // No need for constructor injection here
    // Add any global security settings if needed, but no bean references
//    private final CorsConfigurationSource corsConfigurationSource;
//    private final SecurityFilterChain securityFilterChain;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    public WebSecurityConfig(CorsConfigurationSource corsConfigurationSource,
//                             SecurityFilterChain securityFilterChain,
//                             PasswordEncoder passwordEncoder,
//                             AuthenticationManager authenticationManager)
//    {
//        this.corsConfigurationSource = corsConfigurationSource;
//        this.securityFilterChain = securityFilterChain;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//    }
}

//***************************************************************************************