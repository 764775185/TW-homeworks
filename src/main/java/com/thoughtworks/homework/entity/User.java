package com.thoughtworks.homework.entity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(unique = true)
    @Size(max = 50)
    private String username;

    @NonNull
    @Max(100)
    private int age;

    @NonNull
    @Size(max = 50)
    private String gender;

    public User(){

    }

    public User(String username, int age, String gender) {
        this.username = username;
        this.age = age;
        this.gender = gender;
    }
}
