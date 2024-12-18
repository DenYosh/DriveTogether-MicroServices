package com.drivetogether.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RideDTO {
    private Long id;
    private int availableSeats;
    private boolean completed;
}
