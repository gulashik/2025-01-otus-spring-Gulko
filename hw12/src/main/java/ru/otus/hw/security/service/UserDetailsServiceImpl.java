package ru.otus.hw.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.security.model.AuthenticatedUserDetails;
import ru.otus.hw.security.model.Authority;
import ru.otus.hw.security.repository.UserRepository;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // An extension of the UserDetailsService which provides the ability to create new users and update existing ones.
    private final UserDetailsManager userDetailsManager;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        AuthenticatedUserDetails authenticatedUser = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));

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

    @Transactional(rollbackFor = Exception.class)
    public UserDetails createUser(String username, String rawPassword, String... authorities) {

        if (userRepository.findByUsername(username).isEmpty()) {
            UserDetails user = User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(authorities)
                .build();
            userDetailsManager.createUser(user);
        }

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Something went wrong " + username));
    }
}