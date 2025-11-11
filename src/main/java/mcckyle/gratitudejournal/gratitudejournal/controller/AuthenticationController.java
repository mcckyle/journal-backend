//***************************************************************************************
//
//   Filename: AuthenticationController.java
//   Author: Kyle McColgan
//   Date: 10 November 2025
//   Description: This file provides register and login functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.controller;

import mcckyle.gratitudejournal.gratitudejournal.dto.UserAuthenticationDTO;
import mcckyle.gratitudejournal.gratitudejournal.dto.UserRegistrationDTO;
import mcckyle.gratitudejournal.gratitudejournal.model.User;
import mcckyle.gratitudejournal.gratitudejournal.payload.JwtResponse;
import mcckyle.gratitudejournal.gratitudejournal.security.UserDetailsImpl;
import mcckyle.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import mcckyle.gratitudejournal.gratitudejournal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;

//***************************************************************************************

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO)
    {
        try
        {
            // Register the user using UserService with the new DTO
            User registeredUser = userService.registerUser(userRegistrationDTO);  // Pass DTO instead of RegisterRequest

            // Instead of authenticating the user immediately, just return success
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully.");
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace(); // For debugging purposes
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Registration error occurred: ", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody UserAuthenticationDTO authenticationDTO)
    {
        // Authenticate the user using the provided credentials from DTO
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get the user details (from UserAuthenticationService)
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities());

        // Create and return JWT response
        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities()
        );

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader)
    {
        try
        {
            // Expect "Bearer <token>".
            if ( (authHeader == null) || (!authHeader.startsWith("Bearer ")) )
            {
                return ResponseEntity.badRequest().body(Map.of("valid", false, "error", "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);

            if (!jwtUtils.validateJwtToken(token))
            {
                return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid or expired token."));
            }

            // Extract username from valid token.
            String username = jwtUtils.getUserNameFromJwtToken(token);

            // Verify user still exists in DB.
            boolean userExists = userService.findByUsername(username).isPresent();
            if (!userExists)
            {
                return ResponseEntity.status(404).body(Map.of("valid", false, "error", "User not found."));
            }

            return ResponseEntity.ok(Map.of("valid", true, "username", username));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(Map.of("valid", false, "error", "Server error: " + e.getMessage()));
        }
    }
}

//***************************************************************************************
