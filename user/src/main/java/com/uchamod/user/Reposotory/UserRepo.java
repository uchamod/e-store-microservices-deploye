package com.uchamod.user.Reposotory;

import com.uchamod.user.Model.User;

import com.uchamod.user.Model.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface UserRepo extends JpaRepository<User,UUID> {
    User findByUserEmail(String userEmail);
    User findByUserPhone(String phone);

    List<User> findUserByUserRole(String role);
}
