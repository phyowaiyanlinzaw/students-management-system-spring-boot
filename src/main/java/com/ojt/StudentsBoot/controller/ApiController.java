package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.StudentService;
import com.ojt.StudentsBoot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final UserService userService;
    private final CourseService courseService;
    private final StudentService studentService;

    @RequestMapping(value = "/get-users",method = RequestMethod.GET)
    public DataTablesOutput<User> getUsers(@Valid DataTablesInput input) {
        return userService.findAll(input);
    }

    @GetMapping( "/get-courses")
    public DataTablesOutput<Course> getCourses(@Valid DataTablesInput input) {
        log.info("in getCourses API");
        return courseService.findAll(input);
    }

    @RequestMapping(value="/get-students",method = RequestMethod.GET)
    public DataTablesOutput<Student> getStudents(@Valid DataTablesInput input){
        return studentService.findAll(input);
    }

    @PostMapping("/toggle-user-active/{id}")
    public ResponseEntity<String> toggleUserActive(@PathVariable Long id, @RequestParam Boolean status) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setEnabled(status);
        userService.save(user);

        return ResponseEntity.ok("User status toggled successfully");
    }

    @PostMapping("/toggle-student-active/{id}")
    public ResponseEntity<String> toggleStudentActive(@PathVariable Long id, @RequestParam Boolean status) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        student.setEnabled(status);
        studentService.save(student);

        return ResponseEntity.ok("User status toggled successfully");
    }
}
