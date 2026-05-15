package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="milestone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Milestone {

    @Id @Column(name="milestone_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Column(name="status", nullable = false)
    private MilestoneStatus status;

    @ManyToOne @NotNull
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    @Builder
    public Milestone(String name, MilestoneStatus status, Project project) {
        this.name = name;
        this.status = status;
        this.project = project;
    }

    public void update(String name, MilestoneStatus status) {
        this.name = name;
        this.status = status;
    }
}
