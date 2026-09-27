package com.example.castone2rifqa.Service;

import com.example.castone2rifqa.Api.ApiException;
import com.example.castone2rifqa.Entity.User;
import com.example.castone2rifqa.Entity.UserStatus;
import com.example.castone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found with ID: " + id);
        }
        return user;
    }

    public List<User> getUsersByCity(String city) {
        List<User> users = userRepository.findUsersByCity(city);
        if (users.isEmpty()) {
            throw new ApiException("No users found in city: " + city);
        }
        return users;
    }

    public List<User> getUsersByStatus(UserStatus status) {
        List<User> users = userRepository.findUsersByStatus(status);
        if (users.isEmpty()) {
            throw new ApiException("No users found with status: " + status);
        }
        return users;
    }

    public Boolean addUser(User user) {
        User existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new ApiException("Email is already in use");
        }

        user.setIsVerified(false);
        user.setStatus(UserStatus.LOOKING);
        userRepository.save(user);
        return true;
    }

    public Boolean updateUser(Integer id, User updatedUser) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found with ID: " + id);
        }

        User emailCheck = userRepository.findUserByEmail(updatedUser.getEmail());
        if (emailCheck != null && !emailCheck.getId().equals(id)) {
            throw new ApiException("Email is already taken by another user");
        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAge(updatedUser.getAge());
        user.setGender(updatedUser.getGender());
        user.setCity(updatedUser.getCity());
        user.setOccupation(updatedUser.getOccupation());
        user.setBio(updatedUser.getBio());
        user.setBudget(updatedUser.getBudget());
        user.setSmoker(updatedUser.getSmoker());
        user.setHasPets(updatedUser.getHasPets());
        user.setAllowsVisitors(updatedUser.getAllowsVisitors());
        user.setCleanlinessLevel(updatedUser.getCleanlinessLevel());
        user.setSleepSchedule(updatedUser.getSleepSchedule());
        user.setStatus(updatedUser.getStatus());

        userRepository.save(user);
        return true;
    }

    public Boolean deleteUser(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found with ID: " + id);
        }
        userRepository.delete(user);
        return true;
    }
}