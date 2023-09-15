package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.CustomerUserDetails;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.repository.RoleRepository;
import com.ojt.StudentsBoot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if (username.contains("@")) {
            // Find user by email
            User user = userRepository.findByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("Could not find user");
            }

            System.out.println("User: " + user.getEmail() + " " + user.getRoles());

            return new CustomerUserDetails(user);
        }

        // Find user by username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        System.out.println("User: " + user.getUsername() + " " + user.getRoles());

        return new CustomerUserDetails(user);
    }
    public User save(User user) {
        user.setEnabled(true);
        user.setPassword("12345");
        return userRepository.save(user);
    }

    public DataTablesOutput<User> findAll(DataTablesInput input) {
        return userRepository.findAll(input);
    }


    public List<User> getUserByRole(String role) {
        Role targetRole = roleRepository.findByName(role);
        return userRepository.findAll().stream().filter(user -> user.getRoles().contains(targetRole)).collect(Collectors.toList());
    }

    public Long getTeachersCount() {
        return userRepository.count();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
