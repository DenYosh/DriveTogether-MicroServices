package com.drivetogether.bookingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RideFullDTO {
    private String source;
    private String destination;
    private LocalDateTime startTime;
    private Boolean completed;
}
