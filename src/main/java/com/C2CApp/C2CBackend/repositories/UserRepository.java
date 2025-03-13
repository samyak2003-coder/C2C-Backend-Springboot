package com.C2CApp.C2CBackend.repositories;

import com.C2CApp.C2CBackend.entities.UserSchema;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserSchema, String> {
    UserSchema findByEmail(String email);
}
