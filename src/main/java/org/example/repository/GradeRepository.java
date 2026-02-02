package org.example.repository;
import org.example.domain.Grade;
import org.example.domain.Student;
import org.example.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentLastNameContainingIgnoreCase(String lastName);

    List<Grade> findByStudentLastNameContainingIgnoreCaseAndSubjectId(
            String lastName,
            Long subjectId
    );

    List<Grade> findBySubjectId(Long subjectId);
}
