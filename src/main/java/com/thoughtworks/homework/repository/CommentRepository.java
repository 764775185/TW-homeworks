package com.thoughtworks.homework.repository;

import com.thoughtworks.homework.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comments,Integer> {

    @Query(value = "select * from Comments order by timestamp desc ",nativeQuery = true)
    Iterable<Comments> findAllOderByDesc();
}