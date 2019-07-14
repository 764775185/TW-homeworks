package com.thoughtworks.homework.repository;

import com.thoughtworks.homework.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRespository extends JpaRepository<Post,Integer> {

}
