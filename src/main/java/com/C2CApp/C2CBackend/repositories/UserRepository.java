package com.C2CApp.C2CBackend.repositories;

import com.C2CApp.C2CBackend.entities.UserSchema;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserSchema, String> {
    UserSchema findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserSchema u WHERE u.id = :userId")
    void deleteById(@Param("userId") String userId);
}
