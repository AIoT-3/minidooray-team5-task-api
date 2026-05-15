package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByProject_Id(Long projectId);
}
