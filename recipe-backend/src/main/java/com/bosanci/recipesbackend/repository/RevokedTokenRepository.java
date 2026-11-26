package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {

    @Modifying
    @Query("delete from RevokedToken t where t.expiresAt < :now")
    void deleteExpired(@Param("now") LocalDateTime now);
}