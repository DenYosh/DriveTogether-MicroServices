package com.drivetogether.bookingservice.dto;

import lombok.Data;

@Data
public class RideDTO {
    private Long id;
    private int availableSeats;
    private boolean completed;
}
