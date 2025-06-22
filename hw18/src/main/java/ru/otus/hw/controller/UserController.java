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
     * @return ResponseEntity со списком пользователей
     */
    @RateLimiter(name = "userApi")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String search) {
        logger.info("Request to get all users, search parameter: {}", search);

        List<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search);
            logger.info("Retrieved {} users matching search term: {}", users.size(), search);
        } else {
            users = userService.getAllUsers();
            logger.info("Retrieved {} users", users.size());
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity с пользователем
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Request to get user by id: {}", id);

        return userService.getUserById(id)
            .map(user -> {
                logger.info("Retrieved user with id: {}", id);
                return ResponseEntity.ok(user);
            })
            .orElseGet(() -> {
                logger.warn("User not found with id: {}", id);
                return ResponseEntity.notFound().build();
            });
    }

    /**
     * Создание нового пользователя.
     *
     * @param user данные пользователя
     * @return ResponseEntity с созданным пользователем
     */
    @PostMapping
    @RateLimiter(name = "createOperations")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        logger.info("Request to create user with email: {}", user.getEmail());

        User createdUser = userService.createUser(user);
        logger.info("Created user with id: {}", createdUser.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Обновление пользователя.
     *
     * @param id идентификатор пользователя
     * @param userDetails новые данные пользователя
     * @return ResponseEntity с обновленным пользователем
     */
    @PutMapping("/{id}")
    @RateLimiter(name = "userApi")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        logger.info("Request to update user with id: {}", id);

        User updatedUser = userService.updateUser(id, userDetails);
        logger.info("Updated user with id: {}", id);
        
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Удаление пользователя.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity с результатом операции
     */
    @DeleteMapping("/{id}")
    @RateLimiter(name = "userApi")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        logger.info("Request to delete user with id: {}", id);
        
        userService.deleteUser(id);
        logger.info("Deleted user with id: {}", id);
        
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}