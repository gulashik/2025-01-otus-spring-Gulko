package ru.otus.hw.indicator.health;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyCustomHealthIndicatorTest {

    private static MyCustomHealthIndicator healthIndicator;
    private static Random mockRandom;

    @BeforeAll
    static void setUp() throws Exception {
        healthIndicator = new MyCustomHealthIndicator();
        Field randomField = MyCustomHealthIndicator.class.getDeclaredField("randomizer");
        randomField.setAccessible(true);
        mockRandom = mock(Random.class);
        randomField.set(healthIndicator, mockRandom);
    }

    @Test
    void healthShouldReturnUpWhenSystemIsUp() {
        // arrange
        when(mockRandom.nextBoolean()).thenReturn(true);
        
        // act
        Health health = healthIndicator.health();
        
        // assert
        assertEquals(Status.UP, health.getStatus());
        assertEquals("Система работает нормально", health.getDetails().get("message"));
        assertEquals("OK", health.getDetails().get("status"));
    }

    @Test
    void healthShouldReturnDownWhenSystemIsDown() {
        // arrange
        when(mockRandom.nextBoolean()).thenReturn(false);
        
        // act
        Health health = healthIndicator.health();
        
        // assert
        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Система недоступна", health.getDetails().get("message"));
        assertEquals("ERROR", health.getDetails().get("status"));
    }
}