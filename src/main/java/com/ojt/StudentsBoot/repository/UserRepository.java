package com.ojt.StudentsBoot.repository;

import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, DataTablesRepository<User, Long> {

    //    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    User findByUsername(String username);

}
