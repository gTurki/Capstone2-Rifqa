package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// used by add and update listing request endpoints. The listing and requester come from the path, and status is set by the server.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequestDTO {

    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;
}