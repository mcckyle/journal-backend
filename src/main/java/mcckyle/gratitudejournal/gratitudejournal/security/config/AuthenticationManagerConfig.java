//***************************************************************************************
//
//     Filename: AuthenticationManagerConfig.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file implements a custom authentication bean.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

//***************************************************************************************

@Configuration
public class AuthenticationManagerConfig
{

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
    {
        return authConfig.getAuthenticationManager();
    }
}

//***************************************************************************************
