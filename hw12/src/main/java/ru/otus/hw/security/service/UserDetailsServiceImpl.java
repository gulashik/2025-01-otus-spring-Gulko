package ru.otus.hw.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.security.model.AuthenticatedUserDetails;
import ru.otus.hw.security.model.Authority;
import ru.otus.hw.security.repository.UserRepository;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticatedUserDetails authenticatedUser = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return User
            .withUsername(authenticatedUser.getUsername())
            .password(authenticatedUser.getPassword())
            .authorities(
                authenticatedUser.getAuthorities().stream()
                    .map(Authority::getAuthority)
                    .toArray(String[]::new)
            )
            .build();
    }
}