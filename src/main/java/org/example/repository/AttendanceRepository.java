package org.example.repository;

import org.example.domain.Attendance;
import org.example.domain.Student;
import org.example.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByStudentAndSubject(Student student, Subject subject);
    List<Attendance> findBySubject(Subject subject);
    List<Attendance> findBySubjectAndDate(Subject subject, LocalDate date);
    List<Attendance> findByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);

    List<Attendance> findByStudentLastNameContainingIgnoreCaseAndSubjectId(
            String lastName,
            Long subjectId
    );

    List<Attendance> findBySubjectId(Long subjectId);

    List<Attendance> findByStudentGroupNameContainingIgnoreCase(String groupName);

    List<Attendance> findByStudentLastNameContainingIgnoreCase(String lastName);

    List<Attendance> findByStudentGroupNameContainingIgnoreCaseAndSubjectId(
            String groupName,
            Long subjectId
    );

    List<Attendance> findByStudentLastNameContainingIgnoreCaseAndStudentGroupNameContainingIgnoreCase(
            String lastName,
            String groupName
    );

    List<Attendance> findByStudentLastNameContainingIgnoreCaseAndStudentGroupNameContainingIgnoreCaseAndSubjectId(
            String lastName,
            String groupName,
            Long subjectId
    );



}