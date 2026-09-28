package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.ChangePasswordDTO;
import com.example.capstone2rifqa.DTO.UserUpdateDTO;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(userService.getUserById(id));
    }

    @GetMapping("/get-by-city/{city}")
    public ResponseEntity<?> getUsersByCity(@PathVariable String city) {
        return ResponseEntity.status(200).body(userService.getUsersByCity(city));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getUsersByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(userService.getUsersByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Password changed successfully"));
    }

    @PutMapping("/update-status/userid/{userId}/status/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer userId, @PathVariable String status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.status(200).body(new ApiResponse("User status updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }
}