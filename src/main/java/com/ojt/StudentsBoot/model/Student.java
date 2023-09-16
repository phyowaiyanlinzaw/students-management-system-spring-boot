package com.ojt.StudentsBoot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dob;
    private String gender;
    private String education;

    @Column(unique = true)
    private String phone;
    private String photoPath;

    @Column(unique = true)
    private String code;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Course> courses = new ArrayList<>();
}
