package org.example.controller;

import org.example.domain.Attendance;
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
    public String list(Model model) {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        model.addAttribute("attendances", attendances);
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
}
