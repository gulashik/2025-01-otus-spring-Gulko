package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.otus.hw.security.SecurityFilterConfiguration;
import ru.otus.hw.service.BookService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.object.TestObjects.getDbBooks;

@WebMvcTest
@Import({SecurityFilterConfiguration.class})
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
                    .with(user("xxx").authorities(new SimpleGrantedAuthority("ROLE_USER")))
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
            .andExpect(status().is3xxRedirection());
    }

    @DisplayName("should access / that match the role hierarchy")
    @ParameterizedTest
    @MethodSource("ControllerRoot")
    void shouldAccessRootThatMatchTheRoleHierarchy(
            String urlTemplate,
            String role,
            ResultMatcher status
        ) throws Exception {

        mockMvc.perform(
                get(urlTemplate)
                    .with(user("xxx").authorities(new SimpleGrantedAuthority(role)))
            )
            .andExpect(status);
    }
    static Stream<Arguments> ControllerRoot() {
        String url = "/";
        return Stream.of(
            Arguments.of(url, "ROLE_USER", status().isOk()),
            Arguments.of(url, "ROLE_MANAGER", status().isOk()),
            Arguments.of(url, "ROLE_ADMIN", status().isOk())
        );
    }

    @DisplayName("should access /edit that match the role hierarchy")
    @ParameterizedTest
    @MethodSource("ControllerEdit")
    void shouldAccessEditThatMatchTheRoleHierarchy(
        String urlTemplate,
        String role,
        ResultMatcher status
    ) throws Exception {
        // arrange
        when(bookService.findById(anyLong())).thenReturn(getDbBooks().get(0));

        // assert
        mockMvc.perform(
                get(urlTemplate)
                    .param("id", "1")
                    .with(user("xxx").authorities(new SimpleGrantedAuthority(role)))
            )
            .andExpect(status);
    }
    static Stream<Arguments> ControllerEdit() {
        String url = "/edit";
        return Stream.of(
            Arguments.of(url, "ROLE_USER", status().isForbidden()),
            Arguments.of(url, "ROLE_MANAGER", status().isOk()),
            Arguments.of(url, "ROLE_ADMIN", status().isOk())
        );
    }

    @DisplayName("should access /delete that match the role hierarchy")
    @ParameterizedTest
    @MethodSource("ControllerDelete")
    void shouldAccessDeleteThatMatchTheRoleHierarchy(
        String urlTemplate,
        String role,
        ResultMatcher status
    ) throws Exception {
        // arrange
        doNothing().when(bookService).deleteById(anyLong());

        // assert
        mockMvc.perform(
                get(urlTemplate)
                    .param("id", "1")
                    .with(user("xxx").authorities(new SimpleGrantedAuthority(role)))
            )
            .andExpect(status);
    }
    static Stream<Arguments> ControllerDelete() {
        String url = "/delete";
        return Stream.of(
            Arguments.of(url, "ROLE_USER", status().isForbidden()),
            Arguments.of(url, "ROLE_MANAGER", status().isForbidden()),
            Arguments.of(url, "ROLE_ADMIN", status().is3xxRedirection())
        );
    }
}


