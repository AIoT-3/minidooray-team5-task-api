package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findAllByProject_Id(Long projectId);

    void deleteByProject_Id(Long projectId);
}
