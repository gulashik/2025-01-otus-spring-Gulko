package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.security.model.Authority;
import ru.otus.hw.security.model.User;

import javax.sql.DataSource;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .toArray(String[]::new)
            )
            .build();
    }

        @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // добавить пользователя при старте приложения
        if (!userDetailsManager.userExists("user")) {
            UserDetails user = org.springframework.security.core.userdetails.User
                .builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
            userDetailsManager.createUser(user);
        }
        return userDetailsManager;
    }
}

//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    public UserDetailsServiceImpl(UserRepository repository) {
//        this.repository = repository;
//    }
//
//    private final UserRepository repository;
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) {
//        return repository.findByUsername(username)
//            .orElseThrow(() -> new EntityNotFoundException("Пользователь %s не найден".formatted(username)));
//    }
//
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        // добавить пользователя при старте приложения
//        if (!userDetailsManager.userExists("user")) {
//            UserDetails user = User
//                .builder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//            userDetailsManager.createUser(user);
//        }
//        return userDetailsManager;
//    }
//
////    @Bean
////    public InMemoryUserDetailsManager userDetailsService() {
////        UserDetails user = User
////            .builder()
////            .username("user")
////            .password("password")
////            .roles("USER")
////            .build();
////        return new InMemoryUserDetailsManager(user);
////    }
//}
