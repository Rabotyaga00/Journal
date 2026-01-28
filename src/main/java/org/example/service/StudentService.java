package org.example.service;

import org.example.domain.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    public List<Student> getStudentsByGroup(String groupName) {
        return studentRepository.findByGroupName(groupName);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> search(String query) {
        return studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrGroupNameContainingIgnoreCase(
                        query, query, query
                );
    }

    public List<Student> findByGroup(String groupName) {
        return studentRepository
                .findByGroupNameContainingIgnoreCase(groupName);
    }

}
