package com.drivetogether.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCarsResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<VehicleDTO> vehicles;
}
