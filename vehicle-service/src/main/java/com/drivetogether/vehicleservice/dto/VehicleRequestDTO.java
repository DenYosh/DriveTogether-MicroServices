package com.drivetogether.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRequestDTO {
    private Long ownerId;
    private String licensePlate;
    private String make;
    private Long modelId;
    private Integer capacity;
}
