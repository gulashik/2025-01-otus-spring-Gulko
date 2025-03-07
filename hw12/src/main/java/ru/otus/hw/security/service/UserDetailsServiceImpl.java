package ru.otus.hw.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.model.AuthenticatedUserDetails;
import ru.otus.hw.security.model.Authority;
import ru.otus.hw.security.repository.UserRepository;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<AuthenticatedUserDetails> authenticatedUser = userRepository
            .findByUsername(username);

        return authenticatedUser.map(
            authenticatedUserDetails -> User
                .withUsername(authenticatedUserDetails.getUsername())
                .password(authenticatedUserDetails.getPassword())
                .authorities(
                    authenticatedUserDetails.getAuthorities().stream()
                        .map(Authority::getAuthority)
                        .toArray(String[]::new)
                )
                .build()
        ).orElse(null);
    }
}