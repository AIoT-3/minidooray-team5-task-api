package com.nhnacademy.taskapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Entity @Table(name="project_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember {

    @Id @Column(name="proejct_member_id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @NotNull
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    @NotNull
    @Column(name="user_id", nullable = false)
    private String userId;

    @NotNull
    @Column(name="admin", nullable = false)
    private boolean admin;

    @Builder
    public ProjectMember(Project project, String userId, boolean admin) {
        this.project=project;
        this.userId=userId;
        this.admin=admin;
    }
}
