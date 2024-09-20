package com.flicker.logger.repository;

import com.flicker.logger.entity.BeforeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeforeRepository extends JpaRepository<BeforeEntity, Long> {
}
