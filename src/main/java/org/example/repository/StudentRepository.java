package org.example.repository;

import org.example.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNumber(String studentNumber);
    List<Student> findByGroupName(String groupName);
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrGroupNameContainingIgnoreCase(
            String firstName,
            String lastName,
            String groupName
    );

    List<Student> findByGroupNameContainingIgnoreCase(String groupName);
}
