package com.admin.panel.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.admin.panel.domain.entity.RolesEntity;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {


    Optional<RolesEntity> findByName(String name);
    boolean existsByName(String name);
}