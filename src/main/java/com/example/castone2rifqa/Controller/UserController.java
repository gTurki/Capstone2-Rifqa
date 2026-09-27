package com.example.castone2rifqa.Controller;

import com.example.castone2rifqa.Api.ApiResponse;
import com.example.castone2rifqa.Entity.User;
import com.example.castone2rifqa.Entity.UserStatus;
import com.example.castone2rifqa.Service.UserService;
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
    public ResponseEntity<?> getUsersByStatus(@PathVariable UserStatus status) {
        return ResponseEntity.status(200).body(userService.getUsersByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {
        userService.updateUser(id, user);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }
}