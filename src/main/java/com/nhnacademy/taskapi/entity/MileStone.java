package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name="milestone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileStone {

    @Id @Column(name="milestone_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne @NotNull
    @JoinColumn(name="project_id", nullable = false)
    private Project project;
}
