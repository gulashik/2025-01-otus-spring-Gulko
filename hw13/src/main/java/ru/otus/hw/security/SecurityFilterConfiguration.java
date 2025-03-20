package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.model.AnonymousUserDetails;
import ru.otus.hw.security.service.AuthorizationService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityFilterConfiguration {

    private final UserDetailsManager userDetailsManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(
                AbstractHttpConfigurer::disable
            )
            .sessionManagement(
                (SessionManagementConfigurer<HttpSecurity> session) ->
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .authorizeHttpRequests(
                (authorize) -> authorize
                    // .requestMatchers(HttpMethod.XXX, "/xxx/**").hasRole("XXX")
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/").access(new AuthorizationService().hasAuthorizationGrant("USER"))
                    .requestMatchers("/edit").access(new AuthorizationService().hasAuthorizationGrant("MANAGER"))
                    .requestMatchers("/add","/delete").access(new AuthorizationService().hasAuthorizationGrant("ADMIN"))
                    .anyRequest().denyAll()
            )
            .headers(
                headers -> headers
                    // Разрешаем использование iframe для консоли H2
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .anonymous(
                (AnonymousConfigurer<HttpSecurity> anonConfig) ->
                    anonConfig
                        .principal(new AnonymousUserDetails())
                        .authorities("ROLE_ANONYMOUS")
            )
            .formLogin(
                Customizer.withDefaults()
            )
            .logout(
                (logout) ->
                    logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
            )
            .userDetailsService(
                userDetailsManager
            )
            .build();
    }
}
