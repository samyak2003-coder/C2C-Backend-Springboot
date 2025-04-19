package com.C2CApp.C2CBackend.services;

import com.C2CApp.C2CBackend.repositories.UserRepository;
import com.C2CApp.C2CBackend.schema.UserSchema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserSchema> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserSchema> getUserById(String id) {
        return userRepository.findById(id);
    }

    public UserSchema getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(UserSchema user) {
        userRepository.save(user);
    }

    public void updateUser(String id, UserSchema user) {
        user.setId(id);
        userRepository.save(user);
    }

    public boolean checkPassword(String email, String password) {
        UserSchema user = userRepository.findByEmail(email);
        if (user == null || password == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public boolean isAdmin(String userId) {
        Optional<UserSchema> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserSchema user = userOptional.get();
            return "ADMIN".equals(user.getRole()); 
        }
        return false;
    }
}
