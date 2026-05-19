package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("select m from ProjectMember m where m.project.id=:projectId and m.userId=:userId")
    Optional<ProjectMember> findByProject_IdAndUserId(Long projectId, String userId);

    List<ProjectMember> findAllByProject_Id(Long projectId);

    boolean existsByProject_IdAndUserId(Long projectId, String userId);
}
