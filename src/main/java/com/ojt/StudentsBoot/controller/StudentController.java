package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
@Slf4j
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
    ) {
        Long count = studentService.getStudentsCount();
        Student student = new Student();
        student.setCode(String.format("STU%04d",count+1));
        modelMap.addAttribute("courses",courseService.findAllByEnabledTrue());
        return new ModelAndView("student-add","student",student);
    }

    @PostMapping("/add")
    public String addStudent(
            @ModelAttribute("student") Student student,
            RedirectAttributes redirectAttributes,
            @RequestParam("photo") MultipartFile file
            ){


         UUID randomUUID= UUID.randomUUID();

        try{
            if (!file.isEmpty()){
                file.transferTo(Path.of("src/main/resources/static/images/"+randomUUID+"_"+file.getOriginalFilename()));
            }
            student.setPhotoPath("/images/"+randomUUID+"_"+file.getOriginalFilename());
            student.setEnabled(true);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("success","studentAddSuccess");
        }   catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("error","studentDupeError");
            return "redirect:/student/add";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/student/list";
    }

    @GetMapping("/edit/{id}")
    public String studentEditView(
            @PathVariable Long id,
            ModelMap modelMap
    ){
        modelMap.addAttribute("courses",courseService.findAllByEnabledTrue());
        modelMap.addAttribute("student",studentService.findById(id));
        return "student-edit";
    }

    @PostMapping("/edit")
    public String studentEdit(
            @ModelAttribute("student") Student student,
            RedirectAttributes redirectAttributes,
            @RequestParam("photo") MultipartFile file
    ){
        try{
            UUID randomUUID =UUID.randomUUID();
            if (!file.isEmpty()){
                file.transferTo(Path.of("src/main/resources/static/images/"+randomUUID+"_"+file.getOriginalFilename()));
            }
            student.setPhotoPath("/images/"+randomUUID+"_"+file.getOriginalFilename());
            student.setEnabled(true);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("success","studentEditSuccess");
        }   catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("error","studentDupeError");
            return "redirect:/student/edit/"+student.getId().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/student/list";
    }

}
