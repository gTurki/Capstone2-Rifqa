package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.ChangePasswordDTO;
import com.example.capstone2rifqa.DTO.UserUpdateDTO;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Entity.UserStatus;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public List<User> getUsersByStatus(String status) {
        UserStatus userStatus;

        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid status: " + status + ". Allowed values: " + Arrays.toString(UserStatus.values()));
        }

        List<User> users = userRepository.findUsersByStatus(userStatus);
        if (users.isEmpty()) {
            throw new ApiException("No users found with status: " + userStatus);
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

    // Profile fields only. Password and status have their own endpoints.
    public Boolean updateUser(Integer id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found with ID: " + id);
        }

        User emailCheck = userRepository.findUserByEmail(userUpdateDTO.getEmail());
        if (emailCheck != null && !emailCheck.getId().equals(id)) {
            throw new ApiException("Email is already taken by another user");
        }

        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        user.setAge(userUpdateDTO.getAge());
        user.setGender(userUpdateDTO.getGender());
        user.setCity(userUpdateDTO.getCity());
        user.setOccupation(userUpdateDTO.getOccupation());
        user.setBio(userUpdateDTO.getBio());
        user.setBudget(userUpdateDTO.getBudget());
        user.setSmoker(userUpdateDTO.getSmoker());
        user.setHasPets(userUpdateDTO.getHasPets());
        user.setAllowsVisitors(userUpdateDTO.getAllowsVisitors());
        user.setCleanlinessLevel(userUpdateDTO.getCleanlinessLevel());
        user.setSleepSchedule(userUpdateDTO.getSleepSchedule());

        userRepository.save(user);
        return true;
    }

    public Boolean changePassword(Integer id, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found with ID: " + id);
        }
        if (!user.getPassword().equals(changePasswordDTO.getOldPassword())) {
            throw new ApiException("Old password is incorrect");
        }
        if (changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword())) {
            throw new ApiException("New password must be different from the old password");
        }

        user.setPassword(changePasswordDTO.getNewPassword());
        userRepository.save(user);
        return true;
    }

    // Users can only switch between LOOKING and NOT_LOOKING. MATCHED is set by the match flow.
    public Boolean updateUserStatus(Integer userId, String status) {
        UserStatus newStatus;
        try {
            newStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid status: " + status + ". Allowed values: [LOOKING, NOT_LOOKING]");
        }

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        if (newStatus == UserStatus.MATCHED) {
            throw new ApiException("Status MATCHED is set automatically when a match is accepted");
        }
        if (user.getStatus() == UserStatus.MATCHED) {
            throw new ApiException("You are currently matched. Remove the match first to change your status");
        }
        if (user.getStatus() == newStatus) {
            throw new ApiException("User status is already " + newStatus);
        }

        user.setStatus(newStatus);
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