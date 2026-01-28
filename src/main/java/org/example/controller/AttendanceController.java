package org.example.controller;

import org.example.domain.Attendance;
import org.example.domain.Student;
import org.example.service.AttendanceService;
import org.example.service.StudentService;
import org.example.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    public AttendanceController(
            AttendanceService attendanceService,
            StudentService studentService,
            SubjectService subjectService
    ) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    /**
     * Общий список посещаемости
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) Long subjectId,
            Model model
    ) {
        List<Attendance> attendances =
                attendanceService.getFilteredAttendances(
                        studentName,
                        groupName,
                        subjectId
                );

        model.addAttribute("attendances", attendances);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("studentName", studentName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("subjectId", subjectId);

        return "attendances/list";
    }



    /**
     * Форма отметки посещаемости
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("date", LocalDate.now());
        return "attendances/form";
    }

    /**
     * Сохранение посещаемости
     */
    @PostMapping
    public String save(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam Boolean present,
            @RequestParam(required = false) String note,
            RedirectAttributes redirectAttributes
    ) {
        attendanceService.saveAttendance(studentId, subjectId, date, present, note);
        redirectAttributes.addFlashAttribute("message", "Посещаемость сохранена");
        return "redirect:/attendances";
    }

    /**
     * Удаление записи
     */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        attendanceService.deleteAttendance(id);
        redirectAttributes.addFlashAttribute("message", "Запись удалена");
        return "redirect:/attendances";
    }

//    @GetMapping("/mark")
//    public String markForm(
//            @RequestParam Long subjectId,
//            Model model
//    ) {
//        model.addAttribute("subject", subjectService.getSubjectById(subjectId)
//                .orElseThrow(() -> new RuntimeException("Subject not found")));
//
//        model.addAttribute("students", studentService.getAllStudents());
//        model.addAttribute("date", LocalDate.now());
//
//        return "attendances/mark";
//    }

    @GetMapping("/mark")
    public String markForm(
            @RequestParam Long subjectId,
            @RequestParam(required = false) String groupName,
            Model model
    ) {
        List<Student> students =
                groupName != null && !groupName.isBlank()
                        ? studentService.findByGroup(groupName)
                        : studentService.getAllStudents();

        model.addAttribute("subject",
                subjectService.getSubjectById(subjectId).orElseThrow());

        model.addAttribute("students", students);
        model.addAttribute("groupName", groupName);
        model.addAttribute("date", LocalDate.now());

        return "attendances/mark";
    }


//    @PostMapping("/mark")
//    public String saveMarked(
//            @RequestParam Long subjectId,
//            @RequestParam LocalDate date,
//            @RequestParam(required = false) List<Long> presentStudents,
//            RedirectAttributes redirectAttributes
//    ) {
//        List<Long> present = presentStudents != null ? presentStudents : List.of();
//
//        studentService.getAllStudents().forEach(student -> {
//            boolean isPresent = present.contains(student.getId());
//
//            attendanceService.saveAttendance(
//                    student.getId(),
//                    subjectId,
//                    date,
//                    isPresent,
//                    null
//            );
//        });
//
//        redirectAttributes.addFlashAttribute("message", "Посещаемость сохранена");
//        return "redirect:/attendances";
//    }

    @PostMapping("/mark")
    public String saveMarked(
            @RequestParam Long subjectId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) List<Long> presentStudents,
            RedirectAttributes redirectAttributes
    ) {
        List<Long> present = presentStudents != null ? presentStudents : List.of();

        List<Student> students =
                (groupName != null && !groupName.isBlank())
                        ? studentService.findByGroup(groupName)
                        : List.of(); // или бросить исключение

        for (Student student : students) {
            boolean isPresent = present.contains(student.getId());

            attendanceService.saveAttendance(
                    student.getId(),
                    subjectId,
                    date,
                    isPresent,
                    null
            );
        }

        redirectAttributes.addFlashAttribute("message", "Посещаемость сохранена");
        return "redirect:/attendances";
    }



}
