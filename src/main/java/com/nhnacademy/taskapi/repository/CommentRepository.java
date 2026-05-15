package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask_Id(Long taskId);

    void deleteByTask_Id(Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.task.project.id = :projectId")
    void deleteAllByProjectIdInBulk(@Param("projectId") Long projectId);
}
