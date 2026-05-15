package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="task_tag")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class TaskTag {

    @Id @Column(name="task_tag_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @NotNull
    @JoinColumn(name="task_id", nullable = false)
    private Task task;

    @ManyToOne @NotNull
    @JoinColumn(name="tag_id", nullable = false)
    private Tag tag;

    @Builder
    public TaskTag(Task task, Tag tag) {
        this.task=task;
        this.tag=tag;
    }
}
