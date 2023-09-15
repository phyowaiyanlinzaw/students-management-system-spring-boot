package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final CourseService courseService;

    @RequestMapping(value = "/getUsers",method = RequestMethod.GET)
    public DataTablesOutput<User> getUsers(@Valid DataTablesInput input) {
        return userService.findAll(input);
    }

    @RequestMapping(value = "/getCourses",method = RequestMethod.GET)
    public DataTablesOutput<Course> getCourses(@Valid DataTablesInput input) {
        return courseService.findAll(input);
    }

    @PostMapping("/toggle-active/{id}")
    public ResponseEntity<String> toggleUserActive(@PathVariable Long id, @RequestParam Boolean status) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setEnabled(status);
        userService.save(user);

        return ResponseEntity.ok("User status toggled successfully");
    }
}
