package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    public Long count(){
        return courseRepository.count();
    }

    public void save(Course course){
        course.setCourseStartDate(new Timestamp(System.currentTimeMillis()));
        courseRepository.save(course);
    }

    public Course findById(Long id){
        return courseRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id){
        courseRepository.deleteById(id);
    }

    public Iterable<Course> findAll(){
        return courseRepository.findAll();
    }

    public DataTablesOutput<Course> findAll(DataTablesInput input){
        return courseRepository.findAll(input);
    }


}
