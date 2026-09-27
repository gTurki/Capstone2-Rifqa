package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);

    User findUserByEmail(String email);

    List<User> findUsersByStatus(UserStatus status);

    List<User> findUsersByCity(String city);

    List<User> findUsersByCityAndStatus(String city, UserStatus status);
}