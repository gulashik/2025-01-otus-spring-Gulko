package ru.otus.hw.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.entity.User;
import ru.otus.hw.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * REST контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение всех пользователей или поиск по параметру.
     *
     * @param search опциональный параметр поиска
     * @return ResponseEntity со списком пользователей или ошибкой
     */
    @RateLimiter(name = "userApi")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String search) {
        logger.info("Request to get all users, search parameter: {}", search);

        try {
            List<User> users;
            if (search != null && !search.trim().isEmpty()) {
                users = userService.searchUsers(search);
                logger.info("Retrieved {} users matching search term: {}", users.size(), search);
            } else {
                users = userService.getAllUsers();
                logger.info("Retrieved {} users", users.size());
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error retrieving users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity с пользователем или ошибкой
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Request to get user by id: {}", id);

        try {
            return userService.getUserById(id)
                .map(user -> {
                    logger.info("Retrieved user with id: {}", id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        } catch (Exception e) {
            logger.error("Error retrieving user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Создание нового пользователя.
     *
     * @param user данные пользователя
     * @return ResponseEntity с созданным пользователем или ошибкой
     */
    @PostMapping
    @RateLimiter(name = "createOperations")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        logger.info("Request to create user with email: {}", user.getEmail());

        try {
            User createdUser = userService.createUser(user);

            logger.info("Created user with id: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create user: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Обновление пользователя.
     *
     * @param id идентификатор пользователя
     * @param userDetails новые данные пользователя
     * @return ResponseEntity с обновленным пользователем или ошибкой
     */
    @PutMapping("/{id}")
    @RateLimiter(name = "userApi")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        logger.info("Request to update user with id: {}", id);

        try {
            User updatedUser = userService.updateUser(id, userDetails);

            logger.info("Updated user with id: {}", id);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update user with id {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Удаление пользователя.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity с результатом операции
     */
    @DeleteMapping("/{id}")
    @RateLimiter(name = "userApi")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Request to delete user with id: {}", id);
        try {
            userService.deleteUser(id);
            logger.info("Deleted user with id: {}", id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to delete user with id {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error deleting user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
        }
    }
}