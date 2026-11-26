package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.TokenType;
import com.bosanci.recipesbackend.model.User;
import com.bosanci.recipesbackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("select t from VerificationToken t join fetch t.user where t.tokenHash = :hash and t.type = :type")
    Optional<VerificationToken> findByHashAndType(@Param("hash") String hash, @Param("type") TokenType type);

    @Modifying
    @Query("delete from VerificationToken t where t.id = :id")
    int deleteByTokenId(@Param("id") Long id);

    @Modifying
    @Query("delete from VerificationToken t where t.user = :user and t.type = :type")
    void deleteAllForUser(@Param("user") User user, @Param("type") TokenType type);
}