package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping("/list")
    public String studentList(){
        return "student-list";
    }

    @GetMapping("/add")
    public ModelAndView studentAddView(
            ModelMap modelMap
    ){
        modelMap.addAttribute("courses",courseService.findAllByDisabledFalse());
        return new ModelAndView("student-add","student",new Student());
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student){
        return "redirect:/student/list";
    }

    @GetMapping("/edit")
    public ModelAndView studentEditView(){
        return new ModelAndView("student-edit","student",new Student());
    }

    @PostMapping("/edit")
    public String studentEdit(@ModelAttribute("student") Student student){
        return "redirect:/student/list";
    }

    @GetMapping("/delete")
    public String deleteStudent(){
        return "redirect:/student/list";
    }
}
