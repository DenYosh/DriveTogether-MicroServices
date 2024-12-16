package com.drivetogether.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private String id;
    private String userId;
    private String rideId;
    private Integer seatsBooked;
    private LocalDateTime bookingTime;
}
