package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    void deleteByTask_Id(Long id);

    void deleteByTag_Id(Long tagId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TaskTag tt WHERE tt.task.project.id = :projectId")
    void deleteAllByProjectIdInBulk(@Param("projectId") Long projectId);
}
