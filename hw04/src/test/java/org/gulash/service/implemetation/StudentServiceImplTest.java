package org.gulash.service.implemetation;

import org.gulash.config.StudentTest;
import org.gulash.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@StudentTest
class StudentServiceImplTest {
    @MockBean
    private LocalizedIOServiceImpl service;

    @Autowired
    private StudentServiceImpl studentService;

    @Test
    void shouldReturnCorrectStudent() {
        // arrange
        String fname = "first_name";
        String lname = "last_name";

        Student expectedStudent = new Student(fname, lname);

        when( service.readStringWithPromptLocalized(any()) )
                .thenReturn(fname)
                .thenReturn(lname);
        // act
        Student actualStudent = studentService.determineCurrentStudent();

        // assert
        assertThat(actualStudent).isEqualTo(expectedStudent);
    }
}