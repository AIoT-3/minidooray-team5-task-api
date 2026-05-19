package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject_Id(Long projectId);

    void deleteByProject_Id(Long projectId);

    List<Task> findAllByMilestone_Id(Long attr0);

    List<Task> findAllByUserId(String memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Task t SET t.milestone = null WHERE t.milestone.id = :milestoneId")
    void updateMilestoneToNullByMilestoneId(@Param("milestoneId") Long milestoneId);

}
