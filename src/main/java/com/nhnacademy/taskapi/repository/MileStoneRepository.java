package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.MileStone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileStoneRepository extends JpaRepository<MileStone, Long> {
}
