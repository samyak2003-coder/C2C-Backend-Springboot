package com.C2CApp.C2CBackend.services.interfaces;

import java.util.List;
import java.util.Optional;
import com.C2CApp.C2CBackend.schema.UserSchema;

public interface IUserService {
    List<UserSchema> getAllUsers();
    Optional<UserSchema> getUserById(String id);
    UserSchema getUserByEmail(String email);
    void createUser(UserSchema user);
    void updateUser(String id, UserSchema user);
    boolean checkPassword(String email, String password);
    void deleteUser(String userId);
    boolean isAdmin(String userId);
}