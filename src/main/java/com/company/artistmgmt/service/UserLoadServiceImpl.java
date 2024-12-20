package com.company.artistmgmt.service;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.exception.ResourceNotFoundException;
import com.company.artistmgmt.model.User;
import com.company.artistmgmt.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoadServiceImpl implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User userByEmail = userRepository.findUserByEmail(username);
            if (userByEmail == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return userByEmail;
        } catch (ArtistException e) {
            throw new RuntimeException(e);
        }
    }
}
