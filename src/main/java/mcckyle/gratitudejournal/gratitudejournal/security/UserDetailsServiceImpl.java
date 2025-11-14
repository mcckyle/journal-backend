//***************************************************************************************
//
//     Filename: UserDetailsServiceImpl.java
//     Author: Kyle McColgan
//     Date: 14 November 2025
//     Description: This file contains database functionality for users.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security;

import mcckyle.gratitudejournal.gratitudejournal.model.User;
import mcckyle.gratitudejournal.gratitudejournal.service.UserRetrievalHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

//***************************************************************************************

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRetrievalHelper userRetrievalHelper;

    @Autowired
    public UserDetailsServiceImpl(UserRetrievalHelper userRetrievalHelper)
    {
        this.userRetrievalHelper = userRetrievalHelper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRetrievalHelper.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username" + username));

        return buildUserDetails(user);
    }

    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException
    {
        User user = userRetrievalHelper.loadUserById(id);

        if (user == null)
        {
            throw new UsernameNotFoundException("User not found with ID: " + id);
        }
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user)
    {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getBio(),
                authorities
        );
    }
}

//***************************************************************************************

