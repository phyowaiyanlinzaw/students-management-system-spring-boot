package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.StudentService;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final CourseService courseService;
    private final StudentService studentService;

    @GetMapping(value = {"/","/dashboard"})
    public String home(ModelMap model){
        Long studentsCount = studentService.getStudentsCount();
        model.addAttribute("studentsCount", studentsCount);
        Long teachersCount = userService.getTeachersCount();
        model.addAttribute("teachersCount", teachersCount);
        Long coursesCount = courseService.count();
        model.addAttribute("coursesCount", coursesCount);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "pages-error-404";
    }
}
