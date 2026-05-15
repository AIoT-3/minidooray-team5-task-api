package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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
    private Milestone mileStone;

    @OneToMany(mappedBy="task")
    private List<TaskTag> taskTags;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    @Builder
    public Task(String title, String content, String userId, Project project) {
        this(title, content, userId, project, null);
    }

    @Builder
    public Task(String title, String content, String userId, Project project, Milestone mileStone) {
        this.title=title;
        this.content=content;
        this.userId=userId;
        this.project=project;
        this.mileStone=mileStone;
    }

    public void update(String title, String content, Milestone mileStone, List<TaskTag> taskTags) {
        this.title = title;
        this.content = content;
        this.mileStone = mileStone;
        this.taskTags = taskTags;
    }
}
