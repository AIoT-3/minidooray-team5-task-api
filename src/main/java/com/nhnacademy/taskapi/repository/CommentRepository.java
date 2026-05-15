package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTask_Id(Long taskId);
}
