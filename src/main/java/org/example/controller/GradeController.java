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
//    @GetMapping
//    public String listGrades(
//            @RequestParam(required = false) Long studentId,
//            @RequestParam(required = false) Long subjectId,
//            Model model
//    ) {
//        List<Grade> grades;
//
//        if (studentId != null && subjectId != null) {
//            grades = gradeService.getGradesByStudentAndSubject(studentId, subjectId);
//        } else if (studentId != null) {
//            grades = gradeService.getGradesByStudent(studentId);
//        } else if (subjectId != null) {
//            grades = gradeService.getGradesBySubject(subjectId);
//        } else {
//            grades = gradeService.getAllGrades();
//        }
//
//        model.addAttribute("grades", grades);
//
//        // 🔥 ВАЖНО — для выпадающих списков
//        model.addAttribute("students", studentService.getAllStudents());
//        model.addAttribute("subjects", subjectService.getAllSubjects());
//
//        // чтобы выбранные значения сохранялись
//        model.addAttribute("studentId", studentId);
//        model.addAttribute("subjectId", subjectId);
//
//        return "grades/list";
//    }

    @GetMapping
    public String listGrades(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) String groupNameForGrades,
            Model model
    ) {
        List<Grade> grades = gradeService.findFiltered(studentName, subjectId);

        model.addAttribute("grades", grades);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("studentName", studentName);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("groupNameForGrades", groupNameForGrades);
        if (groupNameForGrades != null && !groupNameForGrades.isBlank()) {
            model.addAttribute("groupStudents", studentService.findByGroup(groupNameForGrades.trim()));
        }

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

    /** 💾 Сохранить оценки группе (каждому студенту — своя оценка из формы) */
    @PostMapping("/by-group")
    public String saveGradesByGroup(
            @RequestParam Long subjectId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String comment,
            @RequestParam List<Long> studentIds,
            @RequestParam List<String> values,
            RedirectAttributes redirectAttributes
    ) {
        int count = gradeService.saveGradesForGroupStudents(studentIds, values, subjectId, date, comment);
        redirectAttributes.addFlashAttribute("message",
                count > 0 ? "Сохранено оценок: " + count : "Не заполнено ни одной оценки.");
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
