package ru.otus.hw.indicator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MyCustomHealthIndicator implements HealthIndicator {

    private final Random RANDOM = new Random();

    @Override
    public Health health() {

        if (checkIfSystemIsUp()) {
            return Health.up()
                    .withDetail("message", "Система работает нормально")
                    .withDetail("status", "OK")
                    .build();
        } else {
            return Health.down()
                    .withDetail("message", "Система недоступна")
                    .withDetail("status", "ERROR")
                    .build();
        }
    }
    
    private boolean checkIfSystemIsUp() {
        return RANDOM.nextBoolean();
    }
}