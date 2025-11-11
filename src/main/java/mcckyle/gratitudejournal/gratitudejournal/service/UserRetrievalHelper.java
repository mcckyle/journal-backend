//***************************************************************************************
//
//     Filename: UserRetreivalHelper.java
//     Author: Kyle McColgan
//     Date: 02 December 2024
//     Description: This file provides database interaction for loading users.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.service;

import mcckyle.gratitudejournal.gratitudejournal.model.User;
import mcckyle.gratitudejournal.gratitudejournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

//***************************************************************************************

@Component
public class UserRetrievalHelper
{
    private final UserRepository userRepository;

    @Autowired
    public UserRetrievalHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

//***************************************************************************************