package com.thoughtworks.homework.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name="username")
    @Size(max = 50)
    private String username;

    @NotNull
    @Column(name="email",unique = true)
    @Email
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "role")
    @NotNull
    private String role;

    @Column(name = "age")
    @Max(150)
    private int age;

    @Column(name="gender")
    private String gender;

    public User(@Size(max = 50) String username, @NotNull @Email String email, @NotNull String password, @NotNull String role, @Max(150) int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age;
        this.gender = gender;
    }
}
