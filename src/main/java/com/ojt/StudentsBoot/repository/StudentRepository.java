package com.ojt.StudentsBoot.repository;

import com.ojt.StudentsBoot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
}
