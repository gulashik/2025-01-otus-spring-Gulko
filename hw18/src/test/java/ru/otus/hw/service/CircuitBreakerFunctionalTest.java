package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.config.AbstractIntegrationTest;
import ru.otus.hw.entity.User;
import ru.otus.hw.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DisplayName("Circuit Breaker Functional Tests")
class CircuitBreakerFunctionalTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        // Сбрасываем состояние circuit breakers
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(CircuitBreaker::reset);
    }

    @Test
    @DisplayName("UserService Circuit Breaker должен работать при нормальном выполнении")
    void userServiceCircuitBreakerShouldWorkOnSuccessfulExecution() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        CircuitBreaker userServiceCB = circuitBreakerRegistry.circuitBreaker("userService");

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Test User");
        assertThat(userServiceCB.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(userServiceCB.getMetrics().getNumberOfSuccessfulCalls()).isEqualTo(1);
        assertThat(userServiceCB.getMetrics().getNumberOfFailedCalls()).isEqualTo(0);
    }

    @Test
    @DisplayName("UserService Circuit Breaker должен открываться при превышении порога ошибок")
    void userServiceCircuitBreakerShouldOpenOnFailureThreshold() {
        // Given
        when(userRepository.findAll()).thenThrow(new DataAccessException("Database error") {
        });
        CircuitBreaker userServiceCB = circuitBreakerRegistry.circuitBreaker("userService");

        // When - генерируем достаточно ошибок для открытия circuit breaker
        for (int i = 0; i < 5; i++) {
            userService.getAllUsers();
        }

        // Then
        assertThat(userServiceCB.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        assertThat(userServiceCB.getMetrics().getNumberOfFailedCalls()).isEqualTo(5);

        // Следующий вызов должен сразу перейти к fallback без обращения к репозиторию
        List<User> fallbackResult = userService.getAllUsers();
        assertThat(fallbackResult).isEmpty();

        // Проверяем, что репозиторий не вызывался после открытия circuit breaker
        verify(userRepository, times(5)).findAll();
    }

    @Test
    @DisplayName("DatabaseOperations Circuit Breaker должен работать с настройками БД")
    void databaseOperationsCircuitBreakerShouldWorkWithDatabaseSettings() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        CircuitBreaker dbCB = circuitBreakerRegistry.circuitBreaker("databaseOperations");

        // When
        Optional<User> user = userService.getUserById(1L);

        // Then
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("Test User");
        assertThat(dbCB.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(dbCB.getMetrics().getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    @DisplayName("DatabaseOperations Circuit Breaker должен открываться с настройками БД")
    void databaseOperationsCircuitBreakerShouldOpenWithDatabaseSettings() {
        // Given
        when(userRepository.findById(anyLong())).thenThrow(new DataAccessException("Database connection failed") {});
        CircuitBreaker dbCB = circuitBreakerRegistry.circuitBreaker("databaseOperations");

        // When - генерируем ошибки для открытия circuit breaker
        for (int i = 0; i < 3; i++) {
            userService.getUserById(1L);
        }

        // Then
        assertThat(dbCB.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        assertThat(dbCB.getMetrics().getNumberOfFailedCalls()).isEqualTo(3);

        // Следующий вызов должен использовать fallback
        Optional<User> fallbackResult = userService.getUserById(1L);
        assertThat(fallbackResult).isEmpty();

        // Проверяем количество вызовов репозитория
        verify(userRepository, times(3)).findById(anyLong());
    }

    @Test
    @DisplayName("Circuit Breaker должен игнорировать IllegalArgumentException")
    void circuitBreakerShouldIgnoreIllegalArgumentException() {
        // Given
        CircuitBreaker userServiceCB = circuitBreakerRegistry.circuitBreaker("userService");

        // When - вызываем с некорректными параметрами
        try {
            userService.createUser(null);
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
        }

        // Then - IllegalArgumentException должно игнорироваться
        assertThat(userServiceCB.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(userServiceCB.getMetrics().getNumberOfFailedCalls()).isEqualTo(0);
        assertThat(userServiceCB.getMetrics().getNumberOfNotPermittedCalls()).isEqualTo(0);
    }

    @Test
    @DisplayName("Разные Circuit Breaker должны работать независимо")
    void differentCircuitBreakersShouldWorkIndependently() {
        // Given
        when(userRepository.findAll()).thenThrow(new DataAccessException("DB error") {});
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        CircuitBreaker userServiceCB = circuitBreakerRegistry.circuitBreaker("userService");
        CircuitBreaker databaseCB = circuitBreakerRegistry.circuitBreaker("databaseOperations");

        // When - ломаем userService circuit breaker
        for (int i = 0; i < 5; i++) {
            userService.getAllUsers();
        }

        // Then - userService должен быть открыт, databaseOperations - закрыт
        assertThat(userServiceCB.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        assertThat(databaseCB.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        // И databaseOperations должен продолжать работать
        Optional<User> user = userService.getUserById(1L);
        assertThat(user).isPresent();
        assertThat(databaseCB.getMetrics().getNumberOfSuccessfulCalls()).isEqualTo(1);
    }
}