package com.example.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ReportController {



    @Autowired
    private ReportRepository reportRepository;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String search, Model model) {
        List<Report> reports = reportRepository.findAll();

        if (search != null && !search.trim().isEmpty()) {
            reports = reports.stream()
                    .filter(report -> report.getName().contains(search.trim()) || report.getAuthor().contains(search.trim()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("reports", reports);
        model.addAttribute("search", search);
        return "report";
    }

    @PostMapping("/add")
    public String addReport(@ModelAttribute("reports") Report report){
        report.setDate(LocalDate.now());
        reportRepository.save(report);
        return "redirect:/";
    }

    @GetMapping("/search/{id}")
    public String searchReport(@PathVariable("id") Long id, Model model) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + id));
        model.addAttribute("report", report);
        return "report";
    }

    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable("id") Long id){
        reportRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editReport(@PathVariable("id") Long id, Map<String, Object> model) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + id));
        model.put("report", report);
        return "report_edit";
    }

    @PostMapping("/save")
    public String saveReport(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String author,
                             @RequestParam String description) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book id:" + id));

        report.setName(name.trim());
        report.setAuthor(author.trim());
        report.setDescription(description.trim());
        report.setDate(LocalDate.now());

        reportRepository.save(report);
        return "redirect:/";
    }
}

