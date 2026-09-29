package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);

    User findUserByEmail(String email);

    List<User> findUsersByStatus(UserStatus status);

    List<User> findUsersByCity(String city);

    // match candidates: same city, same gender, given status, excluding the user
    @Query("select u from User u where u.city = ?1 and lower(u.gender) = lower(?2) and u.status = ?3 and u.id <> ?4")
    List<User> findMatchCandidates(String city, String gender, UserStatus status, Integer excludedUserId);
}