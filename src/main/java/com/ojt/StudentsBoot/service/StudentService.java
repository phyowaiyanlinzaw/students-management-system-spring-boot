package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Long getStudentsCount() {
        return studentRepository.count();
    }


}
