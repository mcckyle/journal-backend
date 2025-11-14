//***************************************************************************************
//
//     Filename: UserRepository.java
//     Author: Kyle McColgan
//     Date: 14 November 2025
//     Description: This file provides functionality for user-related functionality.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.repository;

//***************************************************************************************

import mcckyle.gratitudejournal.gratitudejournal.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

//***************************************************************************************

public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

//***************************************************************************************