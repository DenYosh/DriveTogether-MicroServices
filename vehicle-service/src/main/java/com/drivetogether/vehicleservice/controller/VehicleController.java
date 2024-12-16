package com.drivetogether.vehicleservice.controller;

import com.drivetogether.vehicleservice.dto.VehicleModelRequestDTO;
import com.drivetogether.vehicleservice.dto.VehicleModelResponseDTO;
import com.drivetogether.vehicleservice.dto.VehicleRequestDTO;
import com.drivetogether.vehicleservice.dto.VehicleResponseDTO;
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


    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.ok(vehicleService.createVehicle(dto));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @PostMapping("/models")
    public ResponseEntity<VehicleModelResponseDTO> createVehicleModel(@RequestBody VehicleModelRequestDTO dto) {
        return ResponseEntity.ok(vehicleService.createVehicleModel(dto));
    }

    @GetMapping("/models")
    public ResponseEntity<List<VehicleModelResponseDTO>> getAllVehicleModels() {
        return ResponseEntity.ok(vehicleService.getAllVehicleModels());
    }
}
