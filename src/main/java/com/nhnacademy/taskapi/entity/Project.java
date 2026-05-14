package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity @Table(name="project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id @Column(name="project_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Column(name="status", nullable = false)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<ProjectMember> memberList=new ArrayList<>();

    @Builder
    public Project(String name) {
        this.name=name;
        this.status=ProjectStatus.ACTIVE;
    }

    public void addMember(ProjectMember member) {
        this.memberList.add(member);
        member.setProject(this);
    }

    public void update(String name, ProjectStatus status) {
        this.name = name;
        this.status = status;
    }
}
