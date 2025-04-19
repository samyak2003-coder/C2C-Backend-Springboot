package com.C2CApp.C2CBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.C2CApp.C2CBackend.schema.UserSchema;

/**
 * Repository interface for User operations.
 * Built-in JPA methods available:
 * - save(UserSchema entity)
 * - findAll()
 * - findById(String id)
 * - deleteById(String id)
 * - existsById(String id)
 * These methods are automatically implemented by Spring Data JPA
 */
@Repository
public interface UserRepository extends JpaRepository<UserSchema, String> {
    @Query("SELECT u FROM UserSchema u WHERE u.email = :email")
    UserSchema findByEmail(@Param("email") String email);
}
