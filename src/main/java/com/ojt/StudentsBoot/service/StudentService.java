package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Long getStudentsCount() {
        return studentRepository.count();
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public DataTablesOutput<Student> findAll(DataTablesInput input) {
        return studentRepository.findAll(input);
    }

}
