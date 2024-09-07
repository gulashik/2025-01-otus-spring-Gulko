package org.gulash.service.implemetation;

import lombok.RequiredArgsConstructor;
import org.gulash.service.LocalizedIOService;
import org.gulash.service.StudentService;
import org.springframework.stereotype.Service;
import org.gulash.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final LocalizedIOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPromptLocalized("StudentService.input.first.name");
        var lastName = ioService.readStringWithPromptLocalized("StudentService.input.last.name");
        return new Student(firstName, lastName);
    }
}
