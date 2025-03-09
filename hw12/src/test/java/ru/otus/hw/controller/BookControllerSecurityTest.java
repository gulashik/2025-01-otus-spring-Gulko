package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.service.BookService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @DisplayName("should works only with authenticated request")
    @Test
    void shouldAccessOnlyAuthenticatedUser() throws Exception {
        // authenticated user - got access
        mockMvc.perform(
                get("/")
                    .with(user("user").authorities(new SimpleGrantedAuthority("ROLE_USER")))
            )
            .andExpect(status().isOk());
    }

    @DisplayName("should not works with non-authenticated users")
    @Test
    void shoulRejectNonAuthenticatedUserRequest() throws Exception {
        // non-authenticated user - access denied
        mockMvc.perform(
                get("/")
            )
            .andExpect(status().isUnauthorized());
    }
}