package com.ojt.StudentsBoot.repository;

import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.model.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends DataTablesRepository<Student, Long> {

}

