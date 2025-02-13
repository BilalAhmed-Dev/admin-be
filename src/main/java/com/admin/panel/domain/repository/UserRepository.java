package com.admin.panel.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.admin.panel.domain.entity.RolesEntity;
import com.admin.panel.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String userEmail);


    List<UserEntity> findAllByRole(RolesEntity role);

}