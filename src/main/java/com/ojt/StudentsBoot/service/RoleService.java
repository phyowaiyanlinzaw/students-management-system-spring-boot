package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }


}
