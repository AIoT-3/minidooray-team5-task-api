package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id @Column(name="tag_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne @NotNull
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    @Builder
    public Tag(String name, Project project) {
        this.name=name;
        this.project=project;
    }
}
