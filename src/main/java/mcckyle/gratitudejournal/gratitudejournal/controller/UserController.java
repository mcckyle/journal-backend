//***************************************************************************************
//
//   Filename: UserController.java
//   Author: Kyle McColgan
//   Date: 12 November 2025
//   Description: This file provides user profile functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.controller;

import mcckyle.gratitudejournal.gratitudejournal.model.User;
import mcckyle.gratitudejournal.gratitudejournal.repository.UserRepository;
import mcckyle.gratitudejournal.gratitudejournal.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    // --- Get Current User. ---
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    // --- Update User Profile. ---
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody Map<String, String> updateRequest)
    {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Update allowed fields.
        if ( (updateRequest.containsKey("username") && ( ! updateRequest.get("username").isBlank())) )
        {
            user.setUsername(updateRequest.get("username"));
        }

        if ( (updateRequest.containsKey("email") && ( ! updateRequest.get("email").isBlank())) )
        {
            user.setEmail(updateRequest.get("email"));
        }

        if ( (updateRequest.containsKey("bio") && ( ! updateRequest.get("bio").isBlank())) )
        {
            user.setBio(updateRequest.get("bio"));
        }

        // Save updated user.
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile updated successfully!");
        response.put("user", user);

        return ResponseEntity.ok(response);
    }
}
