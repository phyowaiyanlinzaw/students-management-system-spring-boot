package com.ojt.StudentsBoot.repository;

import com.ojt.StudentsBoot.model.Course;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>, DataTablesRepository<Course, Long> {
}
