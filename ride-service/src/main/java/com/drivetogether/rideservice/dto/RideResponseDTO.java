package com.drivetogether.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDTO {
    private Long id;
    private Long carId;
    private String source;
    private String destination;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer availableSeats;
    private Boolean completed;
}
