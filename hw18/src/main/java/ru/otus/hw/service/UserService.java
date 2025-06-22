package ru.otus.hw.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.entity.User;
import ru.otus.hw.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями, интегрированный с Resilience4j
 * для обеспечения отказоустойчивости и контроля нагрузки.
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получение всех пользователей с применением паттернов отказоустойчивости.
     *
     * @return список всех пользователей
     * @throws RuntimeException при критических ошибках
     */
    @CircuitBreaker(name = "userService", fallbackMethod = "getAllUsersFallback")
    @Retry(name = "userService")
    @Bulkhead(name = "userService")
    public List<User> getAllUsers() {
        logger.info("Fetching all users with resilience patterns");

        try {
            List<User> users = userRepository.findAll();
            logger.info("Successfully fetched {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            throw e; // Проброс исключения для обработки Resilience4j
        }
    }

    /**
     * Fallback метод для getAllUsers().
     * Вызывается когда Circuit Breaker находится в открытом состоянии
     * или исчерпаны все попытки Retry.
     *
     * @param ex исключение, вызвавшее fallback
     * @return пустой список пользователей
     */
    public List<User> getAllUsersFallback(Exception ex) {
        logger.warn("Fallback activated for getAllUsers due to: {}", ex.getMessage());
        return Collections.emptyList();
    }

    /**
     * Получение пользователя по ID с применением паттернов отказоустойчивости.
     *
     * @param id идентификатор пользователя
     * @return опциональный пользователь
     */
    @CircuitBreaker(name = "databaseOperations", fallbackMethod = "getUserByIdFallback")
    @Retry(name = "databaseOperations")
    @TimeLimiter(name = "databaseOperations")
    @Bulkhead(name = "databaseOperations")
    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);

        if (id == null || id <= 0) {
            logger.warn("Invalid user ID provided: {}", id);
            return Optional.empty();
        }

        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                logger.info("Successfully found user with id: {}", id);
            } else {
                logger.info("No user found with id: {}", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error fetching user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Fallback метод для getUserById().
     *
     * @param id идентификатор пользователя
     * @param ex исключение, вызвавшее fallback
     * @return пустой Optional
     */
    public Optional<User> getUserByIdFallback(Long id, Exception ex) {
        logger.warn("Fallback activated for getUserById({}) due to: {}", id, ex.getMessage());
        return Optional.empty();
    }

    /**
     * Получение пользователя по email.
     *
     * @param email email пользователя
     * @return опциональный пользователь
     */
    @CircuitBreaker(name = "databaseOperations", fallbackMethod = "getUserByEmailFallback")
    @Retry(name = "databaseOperations")
    @Bulkhead(name = "databaseOperations")
    public Optional<User> getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            logger.warn("Invalid email provided");
            return Optional.empty();
        }

        try {
            Optional<User> user = userRepository.findByEmail(email.trim());
            if (user.isPresent()) {
                logger.info("Successfully found user with email: {}", email);
            } else {
                logger.info("No user found with email: {}", email);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error fetching user with email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Fallback метод для getUserByEmail().
     *
     * @param email email пользователя
     * @param ex исключение
     * @return пустой Optional
     */
    public Optional<User> getUserByEmailFallback(String email, Exception ex) {
        logger.warn("Fallback activated for getUserByEmail({}) due to: {}", email, ex.getMessage());
        return Optional.empty();
    }

    /**
     * Поиск пользователей с применением паттернов отказоустойчивости.
     *
     * @param search поисковый запрос
     * @return список найденных пользователей
     */
    @CircuitBreaker(name = "userService", fallbackMethod = "searchUsersFallback")
    @Retry(name = "userService")
    @Bulkhead(name = "userService")
    public List<User> searchUsers(String search) {
        logger.info("Searching users with term: {}", search);

        if (search == null || search.trim().isEmpty()) {
            logger.info("Empty search term, returning all users");
            return getAllUsers();
        }

        try {
            List<User> users = userRepository.findByNameOrEmailContaining(search.trim());
            logger.info("Found {} users matching search term: {}", users.size(), search);
            return users;
        } catch (Exception e) {
            logger.error("Error searching users with term {}: {}", search, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Fallback метод для searchUsers().
     *
     * @param search поисковый запрос
     * @param ex исключение
     * @return пустой список
     */
    public List<User> searchUsersFallback(String search, Exception ex) {
        logger.warn("Fallback activated for searchUsers({}) due to: {}", search, ex.getMessage());
        return Collections.emptyList();
    }

    /**
     * Создание нового пользователя с применением строгих ограничений.
     * Использует отдельный Rate Limiter для операций создания.
     *
     * @param user данные пользователя
     * @return созданный пользователь
     * @throws IllegalArgumentException если пользователь с таким email уже существует
     */
    @CircuitBreaker(name = "userService")
    @Retry(name = "userService")
    @Bulkhead(name = "userService")
    public User createUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        logger.info("Creating new user with email: {}", user.getEmail());

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        try {
            // Проверяем существование пользователя с таким email
            if (userRepository.existsByEmail(user.getEmail())) {
                String errorMsg = "User with email " + user.getEmail() + " already exists";
                logger.warn(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            User savedUser = userRepository.save(user);
            logger.info("User created successfully with id: {}", savedUser.getId());
            return savedUser;
        } catch (IllegalArgumentException e) {
            // Проброс бизнес-логических исключений без retry
            throw e;
        } catch (Exception e) {
            logger.error("Error creating user with email {}: {}", user.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Обновление пользователя с применением паттернов отказоустойчивости.
     *
     * @param id идентификатор пользователя
     * @param userDetails новые данные пользователя
     * @return обновленный пользователь
     * @throws IllegalArgumentException если пользователь не найден или email занят
     */
    @CircuitBreaker(name = "userService")
    @Retry(name = "userService")
    @Bulkhead(name = "userService")
    public User updateUser(Long id, User userDetails) {
        logger.info("Updating user with id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + id);
        }

        if (userDetails == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }

        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

            // Проверяем изменение email
            if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
                String errorMsg = "User with email " + userDetails.getEmail() + " already exists";
                logger.warn(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());

            User updatedUser = userRepository.save(user);
            logger.info("User updated successfully with id: {}", updatedUser.getId());
            return updatedUser;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Удаление пользователя с применением паттернов отказоустойчивости.
     *
     * @param id идентификатор пользователя
     * @throws IllegalArgumentException если пользователь не найден
     */
    @CircuitBreaker(name = "userService")
    @Retry(name = "userService")
    @Bulkhead(name = "userService")
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + id);
        }

        try {
            if (!userRepository.existsById(id)) {
                String errorMsg = "User not found with id: " + id;
                logger.warn(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            userRepository.deleteById(id);
            logger.info("User deleted successfully with id: {}", id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}