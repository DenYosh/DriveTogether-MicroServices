package com.drivetogether.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String licensePlate;
    private String make;
    private String modelName;
    private Integer capacity;
}
