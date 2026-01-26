package org.example.controller;

import org.example.domain.Subject;
import org.example.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listSubjects(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "subjects/list";
    }

    @GetMapping("/new")
    public String showSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjects/form";
    }

    @PostMapping
    public String saveSubject(@ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        subjectService.saveSubject(subject);
        redirectAttributes.addFlashAttribute("message", "Предмет успешно добавлен");
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    public String editSubject(@PathVariable Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        model.addAttribute("subject", subject);
        return "subjects/form";
    }

    @PostMapping("/{id}")
    public String updateSubject(@PathVariable Long id, @ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        subject.setId(id);
        subjectService.saveSubject(subject);
        redirectAttributes.addFlashAttribute("message", "Предмет успешно обновлен");
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/delete")
    public String deleteSubject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        subjectService.deleteSubject(id);
        redirectAttributes.addFlashAttribute("message", "Предмет успешно удален");
        return "redirect:/subjects";
    }
}

