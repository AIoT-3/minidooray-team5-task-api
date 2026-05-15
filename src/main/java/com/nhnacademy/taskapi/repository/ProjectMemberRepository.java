package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("select m from ProjectMember m where m.project.id=:projectId and m.userId=:userId")
    Optional<ProjectMember> findByProject_IdAndUserId(Long projectId, String userId);

    @Modifying
    @Query("delete from ProjectMember m where m.project.id=:projectId")
    void deleteAllByProject_Id(Long projectId);

    @Query("select m from ProjectMember m where m.project.id=:projectId")
    List<ProjectMember> findAllByProject_Id(Long projectId);

    @Query("select case when count(m) > 0 then true else false end from ProjectMember m where m.project.id=:projectId and m.userId=:userId")
    boolean existsByProject_IdAndUserId(Long projectId, String userId);
}
