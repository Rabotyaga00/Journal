package org.example.repository;

import org.example.domain.Attendance;
import org.example.domain.Student;
import org.example.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT a FROM Attendance a
    WHERE (:studentName IS NULL 
           OR a.student.lastName ILIKE CONCAT('%', CAST(:studentName AS string), '%'))
      AND (:groupName IS NULL 
           OR a.student.groupName ILIKE CONCAT('%', CAST(:groupName AS string), '%'))
      AND (:subjectId IS NULL 
           OR a.subject.id = :subjectId)
""")
    List<Attendance> findWithFilters(
            @Param("studentName") String studentName,
            @Param("groupName") String groupName,
            @Param("subjectId") Long subjectId
    );





}