package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id @Column(name="task_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="title", nullable = false)
    private String title;

    @NotNull
    @Column(name="content", nullable = false)
    private String content;

    @NotNull
    @Column(name="user_id", nullable = false)
    private String userId;

    @ManyToOne @NotNull
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name="milestone_id", nullable = true)
    private MileStone mileStone;

    @Builder
    public Task(String title, String content, String userId, Project project) {
        this(title, content, userId, project, null);
    }

    @Builder
    public Task(String title, String content, String userId, Project project, MileStone mileStone) {
        this.title=title;
        this.content=content;
        this.userId=userId;
        this.project=project;
        this.mileStone=mileStone;
    }
}
