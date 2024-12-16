package com.drivetogether.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleOwnerDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}