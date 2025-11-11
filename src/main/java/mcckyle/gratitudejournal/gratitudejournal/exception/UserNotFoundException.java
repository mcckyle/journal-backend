//***************************************************************************************
//
//     Filename: UserNotFoundException.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file implements a custom exception for error messages.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.exception;

//***************************************************************************************

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String message) {
        super(message);
    }
}

//***************************************************************************************
