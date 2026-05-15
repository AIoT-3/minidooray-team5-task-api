package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.dto.project.ProjectResponse;
import com.nhnacademy.taskapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select new com.nhnacademy.taskapi.dto.project.ProjectResponse(p.id, p.name, p.status, m.admin) from Project p join p.memberList m where m.userId=:userId")
    List<ProjectResponse> findProjectResponsesByUserId(String userId);
}
