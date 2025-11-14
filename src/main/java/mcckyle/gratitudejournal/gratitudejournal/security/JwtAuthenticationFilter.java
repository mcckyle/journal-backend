//***************************************************************************************
//
//     Filename: JwtAuthenticationFilter.java
//     Author: Kyle McColgan
//     Date: 14 November 2025
//     Description: This file provides the auth token validation implementation.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security;

//***************************************************************************************

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mcckyle.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//***************************************************************************************

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService; // Change to UserDetailsService

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService)
    {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService; // Inject UserDetailsServiceImpl bean
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String jwt = authHeader.substring(7); // Remove "Bearer " prefix

            if (jwtUtils.validateJwtToken(jwt))
            {
                Integer userId = jwtUtils.getUserIdFromJwtToken(jwt);
                System.out.println("Token validated, userId: " + userId);

                if ( (userId != null) && ( SecurityContextHolder.getContext().getAuthentication() == null) )
                {
                    // Use UserDetailsService to load user details
                    UserDetails userDetails = userDetailsService.loadUserById(userId);

                    // Create authentication token and set it in the context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            else
            {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is invalid or expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

//***************************************************************************************
