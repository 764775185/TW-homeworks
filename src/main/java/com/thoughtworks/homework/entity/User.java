package com.thoughtworks.homework.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    private int age;

    private String gender;

    public User(String username, int age, String gender) {
        this.username = username;
        this.age = age;
        this.gender = gender;
    }
}
