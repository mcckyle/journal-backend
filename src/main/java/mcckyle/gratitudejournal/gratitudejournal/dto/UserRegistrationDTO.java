//***************************************************************************************
//
//     Filename: UserRegistrationDTO.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file holds uer related information in an object.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.dto;

//***************************************************************************************

public class UserRegistrationDTO
{
    private String username;
    private String email;
    private String password;

    // Default constructor
    public UserRegistrationDTO() {}

    // Parameterized constructor
    public UserRegistrationDTO(String username, String email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}

//***************************************************************************************
