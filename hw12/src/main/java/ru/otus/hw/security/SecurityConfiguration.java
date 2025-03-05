package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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
                    .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                    // .requestMatchers(HttpMethod.XXX, "/xxx/**").hasRole("XXX")
                    .anyRequest().authenticated()
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
            .build();
    }

    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/storage.html
    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(10);
        return NoOpPasswordEncoder.getInstance();

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User
            .builder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
