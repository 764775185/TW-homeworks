package com.thoughtworks.homework.repository;

import com.thoughtworks.homework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = " select * from user where username= ?1 ",nativeQuery = true)
    Optional<User> findUser(String username);

}
