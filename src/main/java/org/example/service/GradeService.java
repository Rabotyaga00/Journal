package org.example.service;

import org.example.domain.Grade;
import org.example.domain.Student;
import org.example.domain.Subject;
import org.example.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    public Grade saveGrade(Long studentId, Long subjectId, Integer value, LocalDate date, String comment) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setValue(value);
        grade.setDate(date != null ? date : LocalDate.now());
        grade.setComment(comment);

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public int saveGradesForGroupStudents(List<Long> studentIds, List<String> valueStrs,
                                          Long subjectId, LocalDate date, String comment) {
        if (studentIds == null || studentIds.isEmpty()) {
            return 0;
        }
        if (valueStrs == null) {
            valueStrs = List.of();
        }
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        LocalDate effectiveDate = date != null ? date : LocalDate.now();
        int count = 0;
        for (int i = 0; i < studentIds.size(); i++) {
            String v = i < valueStrs.size() ? valueStrs.get(i) : null;
            if (v == null || v.isBlank()) continue;
            try {
                int value = Integer.parseInt(v.trim());
                if (value < 1 || value > 5) continue;
                saveGrade(studentIds.get(i), subjectId, value, effectiveDate, comment);
                count++;
            } catch (NumberFormatException ignored) { }
        }
        return count;
    }

    public List<Grade> findFiltered(String studentName, Long subjectId) {

        if (studentName != null && !studentName.isBlank() && subjectId != null) {
            return gradeRepository
                    .findByStudentLastNameContainingIgnoreCaseAndSubjectId(studentName, subjectId);
        }

        if (studentName != null && !studentName.isBlank()) {
            return gradeRepository
                    .findByStudentLastNameContainingIgnoreCase(studentName);
        }

        if (subjectId != null) {
            return gradeRepository.findBySubjectId(subjectId);
        }

        return gradeRepository.findAll();
    }

}
