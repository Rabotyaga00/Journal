package org.example.service;

import org.example.domain.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> search(String query) {
        return studentRepository
                .findByFirstNameOrLastNameOrGroupName(
                        query, query, query
                );
    }

    public List<Student> findByGroup(String groupName) {
        return studentRepository
                .findByGroupNameContainingIgnoreCase(groupName);
    }

    public Map<String, List<Student>> groupStudentsByGroup(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        s -> (s.getGroupName() != null && !s.getGroupName().isBlank())
                                ? s.getGroupName()
                                : "Без группы",
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

}
