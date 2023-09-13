package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
