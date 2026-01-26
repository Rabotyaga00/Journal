package org.example.controller;

import org.example.domain.Grade;
import org.example.service.GradeService;
import org.example.service.StudentService;
import org.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    /** 📄 Список всех оценок */
    @GetMapping
    public String listGrades(Model model) {
        List<Grade> grades = gradeService.getAllGrades();
        model.addAttribute("grades", grades);
        return "grades/list";
    }

    /** ➕ Форма добавления оценки */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "grades/form";
    }

    /** 💾 Сохранение оценки */
    @PostMapping
    public String saveGrade(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestParam Integer value,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) LocalDate date,
            RedirectAttributes redirectAttributes
    ) {
        gradeService.saveGrade(studentId, subjectId, value, date, comment);
        redirectAttributes.addFlashAttribute("message", "Оценка успешно сохранена");
        return "redirect:/grades";
    }

    /** 🗑 Удаление оценки */
    @GetMapping("/{id}/delete")
    public String deleteGrade(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gradeService.deleteGrade(id);
        redirectAttributes.addFlashAttribute("message", "Оценка удалена");
        return "redirect:/grades";
    }
}
