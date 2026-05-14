package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @Column(name="comment_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="user_id", nullable = false)
    private String userId;

    @NotNull
    @Column(name="content", nullable = false)
    private String content;

    @ManyToOne @NotNull
    @JoinColumn(name="task_id", nullable = false)
    private Task task;

    @Builder
    public Comment(String userId, String content, Task task) {
        this.userId=userId;
        this.content=content;
        this.task=task;
    }
}
