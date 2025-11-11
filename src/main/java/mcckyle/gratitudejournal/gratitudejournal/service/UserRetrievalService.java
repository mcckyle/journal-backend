//***************************************************************************************
//
//     Filename: UserRetrevialService.java
//     Author: Kyle McColgan
//     Date: 02 December 2024
//     Description: This file provides shared user functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.service;

import mcckyle.gratitudejournal.gratitudejournal.model.User;
import java.util.Optional;

//***************************************************************************************

public interface UserRetrievalService
{
    Optional<User> findByUsername(String username);
}

//***************************************************************************************
