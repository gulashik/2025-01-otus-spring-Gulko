package ru.otus.hw.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.model.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
