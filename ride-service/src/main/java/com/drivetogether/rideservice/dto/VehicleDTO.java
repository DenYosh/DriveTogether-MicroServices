package com.drivetogether.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private VehicleOwnerDTO owner;
    private String licensePlate;
    private String make;
    private String modelName;
    private Integer capacity;
}
