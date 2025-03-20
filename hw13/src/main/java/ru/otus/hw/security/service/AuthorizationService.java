package ru.otus.hw.security.service;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.Map;

public class AuthorizationService {
    public AuthorizationManager<RequestAuthorizationContext> hasAuthorizationGrant(String role) {
        return (authentication, context) -> {
            // Проверяем, есть ли у пользователя нужная роль или вышестоящая роль
            String currentUserName = authentication.get().getName();
            List<String> roles = getRolesForUser(currentUserName);

            boolean hasAccess = roles.contains(role);
            return new AuthorizationDecision(hasAccess);
        };
    }

    private List<String> getRolesForUser(String username) {
        // Возвращаем список ролей для пользователя, учитывая иерархию
        Map<String, List<String>> map = Map.of(
            "admin",   List.of("ADMIN", "MANAGER", "USER"),
            "manager", List.of("MANAGER", "USER"),
            "user",    List.of("USER")
        );

        return map.getOrDefault(username, List.of());
    }
}
