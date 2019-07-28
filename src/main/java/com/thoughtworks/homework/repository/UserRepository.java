package com.thoughtworks.homework.repository;

import com.thoughtworks.homework.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findUserByEmail(String email);
}
