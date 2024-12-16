package com.drivetogether.vehicleservice.controller;

import com.drivetogether.vehicleservice.dto.VehicleDTO;
import com.drivetogether.vehicleservice.model.Vehicle;
import com.drivetogether.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/{userId}")
    public ResponseEntity<VehicleDTO> createVehicle(@PathVariable Long userId, @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(userId, vehicleDTO));
    }
}
