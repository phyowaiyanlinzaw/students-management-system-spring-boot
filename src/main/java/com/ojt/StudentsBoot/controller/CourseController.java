package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        }else if (count < 9) {
            course.setCourseId("C00" + (count + 1));
        }else if(count < 99) {
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
    public String courseAdd(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try{
            courseService.save(course);
            redirectAttributes.addFlashAttribute("success", "courseAddSuccess");
        }catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("error", "courseDupe");
            return "redirect:/course/add";
        }

        return "redirect:/course/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView courseEditView(@PathVariable Long id, ModelMap model) {
        Course course = courseService.findById(id);
        List<User> teachers = userService.getUserByRole("TEACHER");
        model.addAttribute("teachers", teachers);
        return new ModelAndView("course-edit", "course", course);
    }

    @PostMapping("/edit")
    public String courseEdit(@ModelAttribute Course course,RedirectAttributes redirectAttributes) {
        courseService.save(course);
        redirectAttributes.addFlashAttribute("success", "courseEditSuccess");
        return "redirect:/course/list";
    }

    @GetMapping("/disable/{id}")
    public String courseDelete(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Course course = courseService.findById(id);
        course.setDisabled(true);
        courseService.save(course);
        redirectAttributes.addFlashAttribute("success", "courseDisableSuccess");
        return "redirect:/course/list";
    }

    @GetMapping("/enable/{id}")
    public String courseEnable(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Course course = courseService.findById(id);
        course.setDisabled(false);
        courseService.save(course);
        redirectAttributes.addFlashAttribute("success", "courseEnableSuccess");
        return "redirect:/course/list";
    }
}
