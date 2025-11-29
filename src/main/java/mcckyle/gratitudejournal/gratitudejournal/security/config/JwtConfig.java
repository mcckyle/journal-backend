//***************************************************************************************
//
//     Filename: JwtConfig.java
//     Author: Kyle McColgan
//     Date: 26 November 2025
//     Description: This file contains the token configuration.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.config;

import mcckyle.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import mcckyle.gratitudejournal.gratitudejournal.service.UserService;
import org.springframework.context.annotation.Configuration;

//***************************************************************************************

@Configuration
public class JwtConfig
{
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtConfig(JwtUtils jwtUtils, UserService userService)
    {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }
}

//***************************************************************************************
