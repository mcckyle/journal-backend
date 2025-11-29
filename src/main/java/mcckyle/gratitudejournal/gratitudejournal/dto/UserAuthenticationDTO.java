//***************************************************************************************
//
//     Filename: UserAuthenticationDTO.java
//     Author: Kyle McColgan
//     Date: 26 November 2025
//     Description: This file provides an authentication object representation.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.dto;

//***************************************************************************************

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserAuthenticationDTO
{
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    public UserAuthenticationDTO() {}

    public UserAuthenticationDTO(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

//***************************************************************************************
