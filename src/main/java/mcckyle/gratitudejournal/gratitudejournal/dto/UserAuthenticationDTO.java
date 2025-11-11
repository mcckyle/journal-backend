//***************************************************************************************
//
//     Filename: UserAuthenticationDTO.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file provides an authentication object representation.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.dto;

//***************************************************************************************

public class UserAuthenticationDTO
{
    private String username;
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
