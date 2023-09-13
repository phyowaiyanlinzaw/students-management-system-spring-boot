package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/list")
    public String courseList() {
        return "course-list";
    }

    @GetMapping("/add")
    public ModelAndView courseAddView(ModelMap model) {
        Long count = courseService.count();
        Course course = new Course();
        if (count < 1) {
            course.setCourseId("C001");
        }else if (count < 10) {
            course.setCourseId("C00" + (count + 1));
        }else if(count < 100) {
            course.setCourseId("C0" + (count + 1));
        }else{
            course.setCourseId("C" + (count + 1));
        }
        List<User> teachers = userService.getUserByRole("TEACHER");
        for (User teacher : teachers) {
            System.out.println(teacher.getUsername());
        }
        model.addAttribute("teachers", teachers);
        return new ModelAndView("course-add", "course", course);
    }

    @PostMapping("/add")
    public String courseAdd(@ModelAttribute Course course) {
        courseService.save(course);
        return "redirect:/course/list";
    }

    @GetMapping("/edit")
    public ModelAndView courseEditView() {
        return new ModelAndView("course-edit", "course", new Course());
    }

    @PostMapping("/edit")
    public String courseEdit(@ModelAttribute Course course) {
        return "redirect:/course/list";
    }

    @GetMapping("/delete")
    public String courseDelete() {
        return "redirect:/course/list";
    }
}
