package com.thoughtworks.homework.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "posts")
public class Posts implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private String timestamp;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "users_id")
    private Users users;

    public Posts(@NotNull String title, String content, String timestamp, @NotNull Users users) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.users = users;
    }
}
