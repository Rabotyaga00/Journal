package org.example.dto;

import org.example.domain.Student;

public record StudentDto(
        Long id,
        String firstName,
        String lastName,
        String groupName
) {
    public static StudentDto from(Student s) {
        return new StudentDto(
                s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getGroupName()
        );
    }
}

