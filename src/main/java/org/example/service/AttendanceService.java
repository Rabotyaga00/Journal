package org.example.service;

import org.example.domain.Attendance;
import org.example.domain.Student;
import org.example.domain.Subject;
import org.example.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getAttendancesByStudent(Long studentId) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudent(student);
    }

    public List<Attendance> getAttendancesByStudentAndSubject(Long studentId, Long subjectId) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return attendanceRepository.findByStudentAndSubject(student, subject);
    }

    public List<Attendance> getAttendancesBySubject(Long subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return attendanceRepository.findBySubject(subject);
    }

    public List<Attendance> getAttendancesBySubjectAndDate(Long subjectId, LocalDate date) {
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return attendanceRepository.findBySubjectAndDate(subject, date);
    }

    public Attendance saveAttendance(Long studentId, Long subjectId, LocalDate date, Boolean present, String note) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectService.getSubjectById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Проверяем, существует ли уже запись на эту дату
        List<Attendance> existing = attendanceRepository.findByStudentAndSubjectAndDate(student, subject, date);
        Attendance attendance;

        if (!existing.isEmpty()) {
            attendance = existing.get(0);
            attendance.setPresent(present);
            attendance.setNote(note);
        } else {
            attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setSubject(subject);
            attendance.setDate(date != null ? date : LocalDate.now());
            attendance.setPresent(present);
            attendance.setNote(note);
        }

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> getFilteredAttendances(Long studentId, Long subjectId) {

        if (studentId != null && subjectId != null) {
            return getAttendancesByStudentAndSubject(studentId, subjectId);
        }

        if (studentId != null) {
            return getAttendancesByStudent(studentId);
        }

        if (subjectId != null) {
            return getAttendancesBySubject(subjectId);
        }

        return getAllAttendances();
    }


}

