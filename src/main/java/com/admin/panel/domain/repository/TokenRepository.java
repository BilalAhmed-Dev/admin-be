package com.admin.panel.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.admin.panel.domain.entity.BlacklistedToken;

import java.util.Date;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<BlacklistedToken, Long> {

    Optional<BlacklistedToken> findByToken(String token);

    void deleteByExpiryDateBefore(Date now);
}