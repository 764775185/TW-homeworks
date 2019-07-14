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
@Table(name = "users")
public class Users implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "role")
    @NotNull
    private String role;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    public Users(@NotNull String username, @NotNull @Email String email, @NotNull String password, int age, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = "ROLE_USER";
        this.age = age;
        this.gender = gender;
    }
}
