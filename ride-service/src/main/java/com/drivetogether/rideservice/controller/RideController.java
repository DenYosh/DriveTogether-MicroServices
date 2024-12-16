package com.drivetogether.rideservice.controller;

import com.drivetogether.rideservice.dto.RideRequestDTO;
import com.drivetogether.rideservice.dto.RideResponseDTO;
import com.drivetogether.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideResponseDTO> createRide(@RequestBody RideRequestDTO rideRequestDTO) {
        return ResponseEntity.ok(rideService.createRide(rideRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<RideResponseDTO> completeRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideService.completeRide(rideId));
    }
}
