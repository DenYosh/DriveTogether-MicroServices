package com.drivetogether.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDTO {
    private Long id;
    private VehicleDTO vehicle;
    private String source;
    private String destination;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableSeats;
    private Boolean completed;
}
