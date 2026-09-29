package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// used by add and update room listing endpoints. the renter comes from the path, and availability is changed through toggle availability endpoint.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListingDTO {

    @NotEmpty(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotEmpty(message = "City is required")
    private String city;

    @NotEmpty(message = "District is required")
    private String district;

    @NotNull(message = "Rent amount is required")
    @PositiveOrZero(message = "Rent amount must be zero or positive")
    private Double rentAmount;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}